package in.apt.assetmanager.service;

import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.models.Response;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Response uploadAsset(MultipartFile file) throws AssetManagerException;

    Response fetchAssetUrlWithTTL(String id,String ttl) throws AssetManagerException;

    Response updateAssetStatus(String assetId, AssetModel asset) throws AssetManagerException;
}
