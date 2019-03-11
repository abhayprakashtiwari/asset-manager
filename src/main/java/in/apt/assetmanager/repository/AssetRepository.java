package in.apt.assetmanager.repository;

import in.apt.assetmanager.entity.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AssetRepository  extends MongoRepository<Asset, Long> {

    Asset findAssetById(Long id);

    Asset findAssetByIdAndStatus(Long id, String status);

    List<Asset> findAssetsByStatus(String status);


}
