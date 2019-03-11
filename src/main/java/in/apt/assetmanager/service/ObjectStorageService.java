package in.apt.assetmanager.service;

public interface ObjectStorageService<T> {

    void upload(String identifier, T t);

    String getExpirableLocation(String identifier, String ttl);

}
