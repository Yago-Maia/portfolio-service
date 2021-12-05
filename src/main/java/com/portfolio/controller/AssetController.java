package com.portfolio.controller;

import com.portfolio.data.vo.AssetVO;
import com.portfolio.service.AssetService;
import com.portfolio.service.CallAssetsService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RestController
@RequestMapping("/asset")
public class AssetController {
    private final AssetService assetService;
    private final PagedResourcesAssembler<AssetVO> assembler;

    @Operation(summary = "Procurar ação por Id", description = "Endpoint para recuperação de ação pelo Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ação encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada") })
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public AssetVO findById(@PathVariable("id") @ApiParam("Id da ação") Long id) {
        AssetVO assetVO = assetService.findById(id);
        assetVO.add(linkTo(methodOn(AssetController.class).findById(id)).withSelfRel());
        return assetVO;
    }

    @Operation(summary = "Procurar ação pelo código", description = "Endpoint para recuperação de ação pelo código")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ação encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada") })
    @GetMapping(value = "/code/{code}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public AssetVO findByCode(@PathVariable("code") @ApiParam("Código da ação") String code) {
        AssetVO assetVO = assetService.findByCode(code);
        assetVO.add(linkTo(methodOn(AssetController.class).findByCode(code)).withSelfRel());
        return assetVO;
    }

    @Operation(summary = "Buscar todas as ações", description = "Endpoint para recuperação de todas as ações")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ações encontradas"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Nenhuma ação cadastrada") })
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") @ApiParam("Número da página") int page,
                                     @RequestParam(value = "limit", defaultValue = "12") @ApiParam("Limite de resultado") int limit,
                                     @RequestParam(value = "direction", defaultValue = "asc") @ApiParam("Direção da ordenação") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "code"));

        Page<AssetVO> assetVOS = assetService.findAll(pageable);
        assetVOS.stream()
                .forEach(a -> a.add(linkTo(methodOn(AssetController.class).findById(a.getId())).withSelfRel()));
        PagedModel<EntityModel<AssetVO>> pagedModel = assembler.toModel(assetVOS);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Criar ação", description = "Endpoint para criar ação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação criada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido")})
    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    @ResponseStatus(HttpStatus.CREATED)
    AssetVO create(@RequestBody @Valid AssetVO assetVO) {
        AssetVO assetVOSaved  = assetService.create(assetVO);
        assetVOSaved.add(linkTo(methodOn(AssetController.class).findById(assetVOSaved.getId())).withSelfRel());

        return assetVOSaved;
    }

    @Operation(summary = "Atualizar ação", description = "Endpoint para editar ação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação editada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada") })
    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    AssetVO update(@RequestBody @Valid AssetVO assetVO) {
        AssetVO assetVOSaved = assetService.update(assetVO);
        assetVOSaved.add(linkTo(methodOn(AssetController.class).findById(assetVOSaved.getId())).withSelfRel());

        return assetVOSaved;
    }

    @Operation(summary = "Deletar ação", description = "Endpoint para deletar ação")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação deletada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Ação não encontrada") })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @ApiParam("Id da ação") Long id) {
        assetService.delete(id);

        return ResponseEntity.ok().build();
    }

}
