package ninjaphenix.userdefinedadditions.api;

@SuppressWarnings("unused")
public interface RegistrableBuilder<T> extends Builder<T>
{
    T register(String id);
}
