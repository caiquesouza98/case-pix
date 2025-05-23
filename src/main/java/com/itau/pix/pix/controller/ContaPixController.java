package com.itau.pix.pix.controller;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.service.ContaPixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/")
@Tag(name = "Chaves PIX", description = "API para gerenciamento de chaves PIX")
public class ContaPixController {

  private final ContaPixService contaPixService;

  @Autowired
  public ContaPixController(ContaPixService contaPixService) {
    this.contaPixService = contaPixService;
  }

  @GetMapping("/find")
  @Operation(
          summary = "Buscar chaves PIX por critérios",
          description = "Permite buscar chaves PIX usando vários critérios de filtragem com suporte a paginação",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Busca realizada com sucesso",
                          content = @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = Page.class)
                          )
                  ),
                  @ApiResponse(responseCode = "400", description = "Parâmetros de busca inválidos"),
                  @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
          }
  )

  public ResponseEntity<Page<ContaPix>> findAllByCriteria(
          @Parameter(description = "ID da chave PIX")
          @RequestParam(required = false) Optional<UUID> id,
          @Parameter(description = "Tipo da chave PIX (CPF, CNPJ, EMAIL, CELULAR, ALEATORIA)")
          @RequestParam(required = false) Optional<TipoChaveEnum> tipoChaveEnum,
          @Parameter(description = "Número da agência")
          @RequestParam(required = false) Optional<String> numAgencia,
          @Parameter(description = "Número da conta")
          @RequestParam(required = false) Optional<String> numConta,
          @Parameter(description = "Nome do titular")
          @RequestParam(required = false) Optional<String> nome,
          @Parameter(description = "Data de inclusão da chave (formato: dd/MM/yyyy)")
          @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Optional<LocalDateTime> dataInclusao,
          @Parameter(description = "Data de inativação da chave (formato: dd/MM/yyyy)")
          @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Optional<LocalDateTime> dataInativacao,
          @Parameter(description = "Configurações de paginação e ordenação")
          @PageableDefault() Pageable pageable) {
    return ResponseEntity.ok(contaPixService.findByCriteria(id, tipoChaveEnum, numAgencia, numConta, nome, dataInclusao, dataInativacao, pageable));
  }

  @PostMapping("/add")
  @Operation(
          summary = "Criar nova chave PIX",
          description = "Cria uma nova chave PIX com as informações fornecidas",
          responses = {
                  @ApiResponse(responseCode = "201", description = "Chave PIX criada com sucesso"),
                  @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                  @ApiResponse(responseCode = "422", description = "Chave PIX já existente")
          }
  )

  public ResponseEntity<ContaPix> savePix(@RequestBody @Valid ContaPixCreateDTO accountDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(contaPixService.save(accountDTO));
  }

  @PutMapping("/update/{id}")
  @Operation(
          summary = "Atualizar chave PIX existente",
          description = "Atualiza uma chave PIX existente com as novas informações",
          responses = {
                  @ApiResponse(responseCode = "200", description = "Chave PIX atualizada com sucesso"),
                  @ApiResponse(responseCode = "404", description = "Chave PIX não encontrada"),
                  @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
          }
  )
  public ResponseEntity<ContaPix> updatePix(@PathVariable UUID id, @RequestBody @Valid ContaPixUpdateDTO accountDTO) {
    return ResponseEntity.ok(contaPixService.update(id, accountDTO));
  }

  @DeleteMapping("/disable/{id}")
  @Operation(
          summary = "Desativar chave PIX",
          description = "Desativa uma chave PIX existente através do seu ID, sem removê-la do sistema",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Chave PIX desativada com sucesso",
                          content = @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = ContaPix.class)
                          )
                  ),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Chave PIX não encontrada"
                  ),
                  @ApiResponse(
                          responseCode = "400",
                          description = "ID inválido fornecido"
                  ),
                  @ApiResponse(
                          responseCode = "500",
                          description = "Erro interno do servidor"
                  )
          }
  )
  public ResponseEntity<ContaPix> deleteAccount(
          @Parameter(
                  description = "ID da chave PIX a ser desativada",
                  required = true,
                  example = "123e4567-e89b-12d3-a456-426614174000"
          )
          @PathVariable UUID id) {
    return ResponseEntity.ok(contaPixService.disableById(id));
  }
}
