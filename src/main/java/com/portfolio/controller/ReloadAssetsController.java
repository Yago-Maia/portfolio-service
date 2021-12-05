package com.portfolio.controller;

import com.portfolio.data.vo.AssetVO;
import com.portfolio.service.CallAssetsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reloadAssets")
public class ReloadAssetsController {

    private final CallAssetsService callAssetsService;
    private final PagedResourcesAssembler<AssetVO> assembler;

    @Operation(summary = "Atualizar as ações da base de dados", description = "Endpoint para atualizar as ações da base de dados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado."),
            @ApiResponse(responseCode = "404", description = "Erro de comunicação.") })
    @GetMapping()
    public List<AssetVO> reloadAssets() throws Exception {
        var listAssetVO = callAssetsService.reloadAssets();
        listAssetVO.forEach(a -> a.add(linkTo(methodOn(AssetController.class).findById(a.getId())).withSelfRel()));

        return listAssetVO;
    }
}
