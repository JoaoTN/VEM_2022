<div align="center">
   <img alt="Logo contest" src="./docs/assets/logotipo.png" style="height: 200px;">
</div>

## Sobre o Projeto
Contest é um sistema da Universidade Federal do Ceará do campus de Quixadá, para controle de eventos, submisões de trabalhos academicos e envio de certificações.

## Objetivo do Experimento
O experimento tem como objetivo identificar os code smell do sistema Contest com JSpiRIT e a ferramenta Understand para verificação das métricas dos atributos de qualidade: Coesão, Acoplamento, Complexidade, Herança e Tamanho.

## Resultados do experimento
Para realização do experimento e identificação da evolução das métricas dos Atributos de qualidade, antes das refatorações e depois de cada refatoração dos code smells foi realizado as médições com as ferramentas JSpiRIT e Understand.

### **Medições**
<ol>
  <li>
    Medição do JSpiRIT antes das refatorações </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="Antes da refatoração parte 1" src="./docs/assets/1.Antes-1.png" style="height: 400px; margin-right: 10px;">
      <img alt="Antes da refatoração parte 2" src="./docs/assets/2.Antes-2.png" style="height: 400px">
    </div>
    </br>
  </li>

  <li>
    Medição do JSpiRIT depois da refatorações do <strong>Feature Envy</strong> </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="Feature Envy" src="./docs/assets/3.Feature Envy.png" style="height: 400px">
    </div>
    </br>
  </li>

  <li>
    Medição do JSpiRIT depois da refatorações do <strong>Shotgun Surgery</strong> </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="Shotgun Surgery" src="./docs/assets/4.Shotgun Surgery.png" style="height: 400px">
    </div>
    </br>
  </li>

  <li>
    Medição do JSpiRIT depois da refatorações do <strong>God Class</strong> </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="God Class" src="./docs/assets/5.God Class.png" style="height: 400px">
    </div>
    </br>
  </li>

  <li>
    Medição do JSpiRIT depois da refatorações do <strong>Intensive Coupling</strong> </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="Intensive Coupling" src="./docs/assets/6.Intensive Coupling.png" style="height: 400px">
    </div>
    </br>
  </li>

  <li>
    Medição do JSpiRIT depois da refatorações do <strong>Dispersed Coupling</strong> </br>
    <div align="center" style="display: flex; flex-direction: columns;">
      <img alt="Dispersed Coupling" src="./docs/assets/7.Dispersed Coupling.png" style="height: 400px">
    </div>
    </br>
  </li>
</ol>

### **Avaliação das medições**
Como resultados das refatorações, podemos observar na tabela a seguir a distribuição das refatorados. Foram restaurados no total 40 instâncias dos 58 code smells identificados, restando apenas 18 no sistema.

<div align="center">
  <img alt="Tabela de refatorações" src="./docs/assets/tabela refatoracoes.png" style="height: 200px">
</div>
</br>

### **Analise dos atributos de qualidade**
Como resultado final das métricas de inferência da qualidade do software, podemos visualizar na tabela a seguir que o sistema obteve uma melhoria no atributo de Complexidade no valor **0,06%**.

<div align="center">
  <img alt="Analise dos atributos de qualidade" src="./docs/assets/atributos de qualidade.png" style="height: 400px">
</div>
</br> </br>

## Analise mais detalhada do experimento
<a href="./docs/Relatorio Contest - Qualidade Software - Entrega final.pdf">Relatorio das refatorações</a>
