package ninjaphenix.userdefinedadditions.api;

public interface RegistrableBuilder<T> extends Builder<T>
{
    T register(String id);
}
