package com.portfolio.service;

import com.portfolio.data.vo.AssetApiVO;
import com.portfolio.data.vo.AssetVO;
import com.portfolio.entity.Asset;
import com.portfolio.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.portfolio.constants.Messages.ID_ASSET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetVO create(AssetVO assetVO) {
        return AssetVO.create(assetRepository.save(Asset.create(assetVO)));
    }

    public Page<AssetVO> findAll(Pageable pageable) {
        var page = assetRepository.findAll(pageable);
        return page.map(this::convertToAssetVO);
    }

    private AssetVO convertToAssetVO(Asset asset){ return AssetVO.create(asset); }

    public AssetVO findById(Long id) {
        var entity = assetRepository.findById(id)
                .orElseThrow(() -> new ResolutionException(ID_ASSET_NOT_FOUND));
        return AssetVO.create(entity);
    }

    public AssetVO findByCode(String code) {
        var entity = assetRepository.findByCode(code)
                .orElseThrow(() -> new ResolutionException(ID_ASSET_NOT_FOUND));
        return AssetVO.create(entity);
    }

    public AssetVO update(AssetVO assetVO) {
        final Optional<Asset> optionalAsset = assetRepository.findById(assetVO.getId());

        if(optionalAsset.isEmpty()) {
            throw new ResolutionException(ID_ASSET_NOT_FOUND);
        }

        return AssetVO.create(assetRepository.save(Asset.create(assetVO)));
    }

    public void delete(Long id) {
        var entity = assetRepository.findById(id)
                .orElseThrow(() -> new ResolutionException(ID_ASSET_NOT_FOUND));

        assetRepository.delete(entity);
    }

    public Asset updateOrSave(AssetVO assetVO) {
        Optional<Asset> asset = assetRepository.findByCode(assetVO.getCode());
        if(asset.isEmpty()) {
            return assetRepository.save(Asset.create(assetVO));
        }

        return asset.get();
    }

    public List<AssetVO> updateAll(List<AssetApiVO> assetApiVOList) {
        var list =  assetApiVOList.stream().map(a -> AssetApiVO.mapperAssetApi(a)).collect(Collectors.toList());
        var listAsset = list.stream().map(this::updateOrSave).collect(Collectors.toList());
        list = listAsset.stream().map(this::convertToAssetVO).collect(Collectors.toList());

        return list;
    }

}
