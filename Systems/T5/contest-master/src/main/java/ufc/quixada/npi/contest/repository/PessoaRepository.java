	package ufc.quixada.npi.contest.repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import ufc.quixada.npi.contest.model.Pessoa;

    import javax.transaction.Transactional;
    import java.util.List;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

	Pessoa findByCpf(String cpf);

	Pessoa findByEmail(String email);

    List<Pessoa> findByNomeContainingIgnoreCaseOrderByNome(String search);

    List<Pessoa> findByCpfOrEmail(String cpf, String email);

    List<Pessoa> findByCpfOrEmailAndIdIsNot(String cpf, String email, Long id);
}