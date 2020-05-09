package ninjaphenix.chainmail.api.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.api.DeserializerFunction;
import blue.endless.jankson.api.Marshaller;
import blue.endless.jankson.api.SyntaxError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public <F> F load(Class<F> configClass, Path configPath, Marker loggerMarker)
    {
        final Path folder = configPath.getParent();
        if (Files.notExists(folder))
        {
            try { Files.createDirectories(folder); }
            catch (IOException e)
            {
                LOGGER.error(loggerMarker, "Cannot create directories needed for config. Using default.");
                return makeDefault(configClass, loggerMarker);
            }
        }
        if (!Files.exists(configPath))
        {
            final F config = makeDefault(configClass, loggerMarker);
            save(config, configPath, loggerMarker);
            return config;
        }
        try (final InputStream configStream = Files.newInputStream(configPath))
        {
            return _jankson.fromJson(_jankson.load(configStream), configClass);
        }
        catch (IOException e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] IO error occurred when loading config.", loggerMarker.getName()), e);
        }
        catch (SyntaxError e)
        {
            throw new RuntimeException(MessageFormat.format("[{0}] Syntax error occurred when loading config.", loggerMarker.getName()), e);
        }
    }

    public <F> void save(F config, Path configPath, Marker loggerMarker)
    {

    }

    private <F> F makeDefault(Class<F> configClass, Marker loggerMarker)
    {
        try { return configClass.newInstance(); }
        catch (InstantiationException | IllegalAccessException e) { throw new RuntimeException("Unable to create new config instance.", e); }
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
