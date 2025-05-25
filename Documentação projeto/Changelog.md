# 📜 Changelog

Todas as mudanças importantes deste projeto serão documentadas aqui.

O formato segue as diretrizes de [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [v2.3.1] - 2025-05-25

### ✨ Adicionado
- Persistência das **alocações** e **indisponibilidades** diretamente no banco de dados.
- Tela de **cadastro de indisponibilidade** de professores integrada ao banco.
- Funcionalidade de **autoalocação** de disciplinas considerando restrições e indisponibilidades dos professores.
- Suporte ao **drag-and-drop** de disciplinas entre lista e grade de horários.
- Botão "Salvar Todas" para registrar alterações em lote.
- Tela para **gerenciamento de professores**, incluindo cadastro, edição, remoção e exibição de disciplinas associadas.
- Suporte ao cadastro de **múltiplas disciplinas** para um mesmo professor.
- Mensagens de **confirmação** e **avisos de erro** nas operações críticas.
- Criação do **DAO de Indisponibilidade** e melhorias nos DAOs de Materia, Professor e Slot.
- Tela para **cadastro e exibição de cursos**, sem filtro obrigatório por semestre.

### 🛠️ Alterado
- Refatoração dos **Controllers** para padronizar chamadas DAO e separar lógica de UI e acesso a dados.
- Melhoria na **atualização dinâmica** de combos e listas (ex: ao selecionar curso/semestre).
- Ajuste no comportamento do **drag-and-drop** para manter a consistência entre listas e slots.
- **Layout das telas** ajustado para melhor alinhamento visual dos botões e controles.
- Otimização das consultas para evitar recarregamentos desnecessários na grade.

### 🐞 Corrigido
- Correção de bug que apagava **cursos** ou **matérias** ao editar dados em sequência.
- Correção no carregamento dos combos de cursos e semestres, que permaneciam **vazios** ou **duplicavam itens**.
- Corrigido problema onde **slots indisponíveis** não eram exibidos corretamente na grade.
- Ajuste no **salvamento da grade** para não sobrescrever alocações previamente existentes.
- Correção no botão **editar/excluir** em Professores, que não atualizava as disciplinas corretamente.
- Correção do **arrasto manual** de disciplinas para garantir que o item volte à lista quando removido do grid.
- Corrigido erro ao tentar alocar disciplina para slot já **ocupado** por outro professor.

## 📅 Histórico de Reuniões

## 🔹 Reuniões Internas da Equipe (Segundas-feiras)
- **05/05/2025**: Planejamento do módulo de indisponibilidade e revisão da estrutura dos DAOs.
- **12/05/2025**: Implementação e testes das telas de cadastro de professores e indisponibilidade.
- **19/05/2025**: Testes do autoalocador e correção de bugs críticos para a entrega.
- **26/05/2025**: Revisão geral, padronização de layouts e testes finais para entrega ao cliente.

## 🔹 Reuniões com o Cliente (Quartas-feiras)
- **07/05/2025**: Demonstração das telas de professores e indisponibilidade.
- **14/05/2025**: Feedback do autoalocador e integração da validação de conflitos.
- **21/05/2025**: Ajustes finais na experiência do usuário e bugs reportados.
- **28/05/2025**: Apresentação da versão estável para homologação final.



## [v1.4.0] - 2025-04-27

### ✨ Adicionado
- Implementação inicial da interface gráfica para gestão de horários usando **JavaFX**.
- Criação do `MontagemGradeController` para montagem dinâmica da grade de horários com **checkboxes**.
- Implementação de **DAOs** para carregar dados de professores e disciplinas.
- Tela de **seleção de horários** com preenchimento automático baseado nos dados de DAO.
- Funcionalidade inicial de **salvar horários selecionados**.
- Estrutura base de **integração futura** com backend para persistência dos dados.

### 🛠️ Alterado
- Ajustes no **layout das telas** para melhor responsividade e melhor experiência do usuário.
- Refatoração da estrutura de pacotes, separando **Controllers**, **DAOs** e **Models**.
- Melhorias de **performance** no carregamento de dados da grade de horários.

### 🐞 Corrigido
- Correção de falha que permitia **seleção múltipla incorreta** de horários.
- Correção de bug onde **checkboxes não atualizavam** ao trocar a disciplina ou professor selecionado.

---

# 📅 Histórico de Reuniões

## 🔹 Reuniões Internas da Equipe (Segundas-feiras)
- **30/03/2025**: Definição inicial do escopo e divisão de tarefas.
- **07/04/2025**: Revisão do protótipo da interface gráfica e ajustes nos DAOs.
- **14/04/2025**: Discussão sobre melhorias de performance e estruturação da montagem da grade.
- **21/04/2025**: Refatoração dos Controllers e definição da estratégia para persistência dos dados.
- **28/04/2025**: Planejamento de melhorias visuais e roadmap de testes de usabilidade.

## 🔹 Reuniões com o Cliente (Quartas-feiras)
- **09/04/2025**: Demonstração do protótipo funcional em aula.
- **16/04/2025**: Coleta de feedback via Slack.
- **23/04/2025**: Validação da estrutura UML.
- **30/04/2025**: Apresentação da versão beta para aprovação e feedback final.

---

# 🚀 Próximos Passos
- Finalizar a **persistência dos dados** selecionados no banco de dados.
- Realizar **testes de usabilidade**.
- Aplicar **correções** baseadas nos feedbacks do cliente.
- **Entrega da primeira versão funcional** prevista para **maio de 2025**.

---

# 🔗 Links Úteis
- [Projeto Principal no GitHub](https://github.com/BugBusters-Suricatos/2nd-Semester)



## [1.3.0] - 2025-03-30
### Adicionado
- *Tela de edição de horários*: Implementação de interface para modificar horários existentes.
- *Validação de entrada de dados*: Adicionado controle para evitar dados inconsistentes nos formulários.
- *Suporte a múltiplos usuários*: Implementado níveis de acesso para diferentes perfis de usuário.

### Melhorado
- *GestorDeHorario.java*: Refatoração do código para otimizar a manipulação de horários.
- *TelaPrincipal.java*: Melhorias na interface gráfica para aprimorar a experiência do usuário.
- *Cobertura de testes*: Aumento dos testes unitários para garantir maior confiabilidade.

### Corrigido
- *Erro ao salvar horários*: Correção de falha ao tentar registrar determinados horários.
- *Conflitos de horário*: Ajuste na exibição e tratamento de horários sobrepostos.
- *Bugs na navegação*: Pequenos ajustes para melhorar a transição entre telas.

### Reunião
- *Data*: 2025-03-25
- *Tópicos discutidos*:
  - Novas funcionalidades e melhorias no módulo de Gestão de Horário.
  - Otimização do código para melhor desempenho.
  - Revisão de testes e cobertura de código.

## [1.2.0] - 2025-03-18
### Adicionado
- *Módulo de Gestão de Horário*: Implementação inicial do módulo utilizando JavaFX para gerenciamento de horários. Foram adicionados os seguintes componentes:
  - *TelaPrincipal.java*: Interface principal para visualização e gerenciamento de horários.
  - *Horario.java*: Classe modelo representando os horários disponíveis.
  - *GestorDeHorario.java*: Classe responsável pela lógica de negócios relacionada ao gerenciamento de horários.

### Reunião
- *Data*: 2025-03-12
- *Tópicos discutidos*:
  - Planejamento da implementação do módulo de gestão de horários.
  - Definição das classes e interfaces necessárias.
  - Estabelecimento de prazos para as próximas etapas do projeto.

## [1.1.0] - 2025-03-11
### Adicionado
- *Documentação*: Criação do arquivo README.md com instruções de instalação, uso e contribuição para o projeto.

### Reunião
- *Data*: 2025-03-05
- *Tópicos discutidos*:
  - Revisão das funcionalidades implementadas até o momento.
  - Discussão sobre a necessidade de uma documentação mais detalhada.
  - Planejamento das próximas funcionalidades a serem desenvolvidas.

## [1.0.0] - 2025-03-04
### Adicionado
- *Versão inicial do projeto*: Estrutura básica do projeto configurada, incluindo:
  - Configuração do ambiente de desenvolvimento.
  - Definição das dependências iniciais.
  - Implementação das primeiras funcionalidades básicas.

### Reunião
- *Data*: 2025-02-26
- *Tópicos discutidos*:
  - Definição do escopo inicial do projeto.
  - Distribuição de tarefas entre os membros da equipe.
  - Estabelecimento de um cronograma para as próximas semanas.
