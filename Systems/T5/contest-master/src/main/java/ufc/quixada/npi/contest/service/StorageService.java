package ufc.quixada.npi.contest.service;

import org.springframework.web.multipart.MultipartFile;
import ufc.quixada.npi.contest.model.Arquivo;

public interface StorageService {
    Arquivo store(MultipartFile file);

    Arquivo load(Integer arquivoId);
    
    Arquivo edit(MultipartFile file, Integer arquivoId);
    
    void delete(Integer arquivoId);
    
}
 