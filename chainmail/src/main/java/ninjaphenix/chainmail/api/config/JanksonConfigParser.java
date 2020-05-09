package ninjaphenix.chainmail.api.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.api.DeserializerFunction;
import blue.endless.jankson.api.Marshaller;
import blue.endless.jankson.api.SyntaxError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.function.BiFunction;

/**
 * Requires Jankson to be JiJ'd separately!
 *
 * @since 0.0.5
 */
public final class JanksonConfigParser
{
    private static final Logger LOGGER = LogManager.getLogger("chainmail-config");
    private final Jankson _jankson;

    public JanksonConfigParser(Jankson jankson) { _jankson = jankson; }

    /**
     * Attempts to load the config from the specified path.
     *
     * @param configClass Config class
     * @param configPath Path to load config from.
     * @param marker Marker for logging with log4j.
     * @return The loaded config.
     */
    public <F> F load(Class<F> configClass, Path configPath, Marker marker)
    {
        final Path folder = configPath.getParent();
        if (Files.notExists(folder))
        {
            try { Files.createDirectories(folder); }
            catch (IOException e)
            {
                throw new RuntimeException(MessageFormat.format("[{0}] Cannot create directories required for config.", marker.getName()), e);
            }
        }
        if (!Files.exists(configPath))
        {
            final F config = makeDefault(configClass, marker);
            if (save(config, configPath, marker))
            {
                throw new RuntimeException(MessageFormat.format("[{0}] Failed to save initial config, look at logs for more info.", marker.getName()));
            }
            return config;
        }
        try (final InputStream configStream = Files.newInputStream(configPath))
        {
            return _jankson.fromJson(_jankson.load(configStream), configClass);
        }
        catch (IOException e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] IO error occurred when loading config.", marker.getName()), e);
        }
        catch (SyntaxError e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] Syntax error occurred when loading config.", marker.getName()), e);
        }
    }

    /**
     * @param config Config to save.
     * @param configPath Path to save config to.
     * @param marker Marker for log4j logging in case error occurs.
     * @return TRUE when config failed to save.
     * @since 0.0.5
     */
    public <F> boolean save(F config, Path configPath, Marker marker)
    {
        try (final BufferedWriter configStream = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))
        {
            configStream.write(_jankson.toJson(config).toJson(JsonGrammar.JSON5));
        }
        catch (IOException e)
        {
            LOGGER.warn(marker, MessageFormat.format("[{0}] IO error occurred whilst saving config.", marker.getName()), e);
            return true;
        }
        return false;
    }

    /* Makes default instance of a config. */
    private <F> F makeDefault(Class<F> configClass, Marker marker)
    {
        try { return configClass.newInstance(); }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] Unable to create new config instance.", marker.getName()), e);
        }
    }


    public final static class Builder
    {
        private final Jankson.Builder _builder;

        public Builder() { _builder = new Jankson.Builder(); }

        public <A, B> Builder registerDeSerializer(Class<A> from, Class<B> to, DeserializerFunction<A, B> deserializer,
                BiFunction<B, Marshaller, JsonElement> serializer)
        {
            _builder.registerSerializer(to, serializer);
            _builder.registerDeserializer(from, to, deserializer);
            return this;
        }

        public JanksonConfigParser build() { return new JanksonConfigParser(_builder.build()); }
    }
}
