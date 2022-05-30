package br.udesc.ceavi.pin.infosaude.view.frame.listener;

import br.udesc.ceavi.pin.infosaude.view.component.campoDeAcao.InternalFrameCadastrarPessoa;
import br.udesc.ceavi.pin.infosaude.view.component.campoDeAcao.InternalFrameCadastrarInstituicao;
import br.udesc.ceavi.pin.infosaude.view.component.campoDeAcao.InternalFrameLogin;
import br.udesc.ceavi.pin.infosaude.view.component.campoDeAcao.InternalFrameTelaInicial;
import br.udesc.ceavi.pin.infosaude.view.frame.FramePrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 * @author Gustavo C. Santos
 */
public class ListenerMenuSemUsuario implements Listener {

    private final FramePrincipal tela;
    private JInternalFrame campoAcaoAtual;
    private JInternalFrame campoAcaoObjetivo;
    private final List<JButton> listaDeButao;

    public ListenerMenuSemUsuario(List<JButton> listaDeButao, FramePrincipal tela) {
        this.listaDeButao = listaDeButao;
        this.tela = tela;
        addActionbtn0();
        addActionbtn1();
        addActionbtn2();
        addActionbtn3();
        addActionbtn4();
    }

    private class btnAction0Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            addPanel(new InternalFrameTelaInicial());
        }
    }

    private class btnAction1Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            addPanel(new InternalFrameLogin(tela));
        }
    }

    private class btnAction2Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            addPanel(new InternalFrameCadastrarPessoa());
        }
    }

    private class btnAction3Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            addPanel(new InternalFrameCadastrarInstituicao());
        }
    }

    private class btnAction4Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            JOptionPane.showMessageDialog(tela, "Telefone : (000) 0800-000 \n"
                    + "Email: infosaude@contato.com");
        }
    }
    
    private void addPanel(JInternalFrame novoFrame) {
    	tela.addPanel(novoFrame);
    }

	@Override
	public void addActionbtn0() {
		listaDeButao.get(0).addActionListener(new btnAction0Listener());
	}

	@Override
	public void addActionbtn1() {
		listaDeButao.get(1).addActionListener(new btnAction1Listener());
		
	}

	@Override
	public void addActionbtn2() {
		listaDeButao.get(2).addActionListener(new btnAction2Listener());
		
	}

	@Override
	public void addActionbtn3() {
		listaDeButao.get(3).addActionListener(new btnAction3Listener());
		
	}

	@Override
	public void addActionbtn4() {
		listaDeButao.get(4).addActionListener(new btnAction4Listener());
		
	}
}
