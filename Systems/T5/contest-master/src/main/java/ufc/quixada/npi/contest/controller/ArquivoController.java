package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.StorageService;

import java.util.Arrays;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {

    @Autowired
    private StorageService storageService;

    @PreAuthorize("#trabalho != null && #trabalho.canViewFile(#usuario)")
    @RequestMapping(value = "/{trabalho}", method = RequestMethod.GET)
    public ResponseEntity<Resource> buscarArquivo(@PathVariable("trabalho")Trabalho trabalho, @AuthenticationPrincipal Pessoa usuario) {
        Arquivo arquivo = storageService.load(trabalho.getArquivo().getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(arquivo.getFormato()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + arquivo.getNome())
                .body(new InputStreamResource(arquivo.getConteudo()));
    }

    @PreAuthorize("#revisao != null && #revisao.canViewFile(#usuario)")
    @RequestMapping(value = "/revisao/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource> buscarArquivoRevisao(@PathVariable("id") Revisao revisao, @AuthenticationPrincipal Pessoa usuario) {
        Arquivo arquivo = storageService.load(revisao.getArquivo().getId());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(arquivo.getFormato()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + arquivo.getNome())
                .body(new InputStreamResource(arquivo.getConteudo()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity uploadArquivo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(storageService.store(file));
    }

}
