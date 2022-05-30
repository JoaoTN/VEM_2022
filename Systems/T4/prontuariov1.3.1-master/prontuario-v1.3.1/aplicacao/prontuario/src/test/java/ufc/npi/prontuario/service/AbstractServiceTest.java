package ufc.npi.prontuario.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

import ufc.npi.prontuario.ProntuarioApplication;

@TestExecutionListeners({DbUnitTestExecutionListener.class})
@SpringBootTest(classes = ProntuarioApplication.class)
public abstract class AbstractServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

}
