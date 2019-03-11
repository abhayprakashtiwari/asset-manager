package in.apt.assetmanager.service.impl;

import in.apt.assetmanager.constants.Constants;
import in.apt.assetmanager.constants.Status;
import in.apt.assetmanager.entity.Asset;
import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.exception.ErrorConstants;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.repository.AssetRepository;
import in.apt.assetmanager.service.AssetService;
import in.apt.assetmanager.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public List<AssetModel> getActiveAssets() {
        List<Asset> activeAssets = assetRepository.findAssetsByStatus(Constants.ACTIVE);
        List<AssetModel> assetModels = ObjectMapper.mapAll(activeAssets, AssetModel.class);
        return assetModels;
    }

    @Override
    public AssetModel saveAsset(MultipartFile file, String fileName){
        Asset asset = Asset.builder().url(fileName)
                .status(Constants.ACTIVE)
                .timestamp(new Timestamp(new Date().getTime()))
                .type(file.getContentType())
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .id(generateUniqueId())
                .build();
        Asset savedAsset = assetRepository.save(asset);
        return ObjectMapper.map(savedAsset, AssetModel.class);
    }

    private Long generateUniqueId(){
        Long securedLongNumber = new SecureRandom().nextLong();
        securedLongNumber = (securedLongNumber< 0) ? -securedLongNumber : securedLongNumber;
        return securedLongNumber;
    }


    @Override
    public AssetModel updateAssetStatus(Long id, AssetModel asset) throws AssetManagerException {
        Status status = validateStatus(asset.getStatus());
        Asset assetById = assetRepository.findAssetById(id);
        if (assetById == null) throw new AssetManagerException(ErrorConstants.INVALID_ASSET_ID, HttpStatus.BAD_REQUEST);
        assetById.setStatus(status.getStatus());
        return ObjectMapper.map(assetRepository.save(assetById), AssetModel.class);
    }

    private Status validateStatus(String statusStr) throws AssetManagerException {
        try {
            Status status = Status.valueOf(statusStr);
            return status;
        } catch (IllegalArgumentException e) {
            throw new AssetManagerException(ErrorConstants.INVALID_STATUS, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String fetchActiveAssetLocation(Long id) throws AssetManagerException {
        Asset asset = assetRepository.findAssetByIdAndStatus(id, Constants.ACTIVE);
        if (asset == null) throw new AssetManagerException(ErrorConstants.INVALID_ASSET_ID, HttpStatus.BAD_REQUEST);
        return asset.getUrl();
    }
}
