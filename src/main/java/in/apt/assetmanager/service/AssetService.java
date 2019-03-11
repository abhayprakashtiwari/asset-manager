package in.apt.assetmanager.service;

import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.models.AssetModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssetService {

    List<AssetModel> getActiveAssets();

    AssetModel saveAsset(MultipartFile file, String name);

    String fetchActiveAssetLocation(Long id) throws AssetManagerException;

    AssetModel updateAssetStatus(Long id, AssetModel asset) throws AssetManagerException;

}
