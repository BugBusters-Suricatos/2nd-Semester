# Documentação Detalhada das User Stories

Este documento apresenta, para cada User Story listada no README.md, o detalhamento completo, incluindo:
- **Descrição**
- **Critérios de Aceitação**
- **Definition of Ready (DoR)**
- **Definition of Done (DoD)**

---

## 1. Definir horários de aula para um semestre
**Como coordenador, eu quero definir os horários de aula para um semestre, para organizar o calendário de aulas.**

### Descrição
Permitir ao coordenador selecionar um semestre e atribuir horários específicos para cada aula de cada disciplina, respeitando a carga horária semanal.

### Critérios de Aceitação
- Deve permitir selecionar o semestre (e exibir as disciplinas vinculadas).
- Exibir lista de disciplinas com a quantidade de aulas semanais.
- Permitir arrastar/distribuir cada aula em slots de 50 minutos disponíveis.
- Validar e alertar sobre horários já ocupados ou indisponibilidades.
- Botão “Salvar” que persiste a alocação no banco de dados.

### Definition of Ready (DoR)
- User Story aprovada pelo Product Owner e priorizada no backlog.
- Desenhos de tela (wireframes ou mockups) disponíveis.
- Modelo ER atualizado para suportar alocação de horários.
- Dependências (DAOs, controllers base) definidas.
- Critérios de aceitação claros e aprovados.

### Definition of Done (DoD)
- Código implementado e commitado no branch da User Story.
- Code review aprovado.
- Cenários de teste unitários cobrindo regras de negócio.
- Testes de integração com o banco passando.
- Validação manual em UI confirmada (alocação e alerta de conflitos).
- Persistência confirmada (banco grava corretamente).
- Documentação técnica (README/Changelog) atualizada.
- Merge para a branch `main` e deploy em ambiente de homologação.

---

## 2. Evitar sobreposição de horários
**Como coordenador, eu quero evitar sobreposição de horários, para garantir que os professores não tenham conflitos de horários.**

### Descrição
Implementar lógica que impeça alocar um professor em dois slots simultâneos, considerando também restrições de indisponibilidade.

### Critérios de Aceitação
- Ao tentar alocar conflito, exibir mensagem de erro e impedir a operação.
- Considerar slots de indisponibilidade cadastrados para o professor.
- Oferecer opção de visualizar detalhes do conflito (professor, disciplina, horário).

### Definition of Ready (DoR)
- User Story priorizada,=.
- Casos de uso.
- Tabelas de indisponibilidade criadas no banco.
- Dependências de slot e professor disponíveis.

### Definition of Done (DoD)
- Validação de conflito implementada no controller.
- Unit tests cobrindo cenário de conflito e sucesso.
- Testes manuais confirmando bloqueio e mensagem correta.
- Documentação de API/DAO atualizada.
- Merge e deploy para homologação.

---

## 3. Receber sugestões de alocação de horários
**Como coordenador, eu quero receber sugestões de alocação de horários, para otimizar a definição do calendário de aulas.**

### Descrição
Disponibilizar algoritmo que analise horários disponíveis, carga horária e indisponibilidades, gerando proposta inicial de alocação.

### Critérios de Aceitação
- Botão “Autoalocar” executa algoritmo de sugestão.
- Exibir proposta em grade, destacando sugestões.
- Permitir ao usuário aceitar, rejeitar ou ajustar manualmente.
- Tratar casos sem solução (ex: falta de slots) com aviso.

### Definition of Ready (DoR)
- Algoritmo desenhado e validado em pseudocódigo.
- Dependências de dados de indisponibilidade e slots prontas.
- Critérios de aceitação revisados.

### Definition of Done (DoD)
- Implementação do algoritmo e integração com UI.
- Testes unitários e de integração no passo de sugestão.
- Validação manual gerando propostas viáveis.
- Atualização de changelog e documentação.

---

## 4. Gerenciar disciplinas de cada semestre
**Como coordenador, eu quero gerenciar as disciplinas de cada semestre, para organizar o cronograma do curso.**

### Descrição
Permitir cadastrar, editar e remover disciplinas, bem como associar cada disciplina ao semestre correto.

### Critérios de Aceitação
- CRUD completo de disciplinas.
- Associação obrigatória de disciplina a um semestre.
- Listagem filtrável por semestre e curso.
- Validação de carga horária e dados obrigatórios.

### Definition of Ready (DoR)
- Tabela `Materia` e `Semestre` criadas e com relação definida.
- Mockups das telas de disciplina prontos.
- Critérios de aceitação aprovados.

### Definition of Done (DoD)
- CRUD implementado com DAOs e controllers.
- Testes unitários cobrindo operações.
- Testes manuais confirmando fluxo completo.
- Documentação atualizada.

---

## 5. Cadastrar e gerenciar cursos e disciplinas
**Como coordenador, eu quero cadastrar e gerenciar cursos e disciplinas, para manter o sistema atualizado.**

### Descrição
Interface para CRUD de cursos e disciplinas, com vínculo entre curso, período e coordenação.

### Critérios de Aceitação
- CRUD de cursos funcional.
- Associação de curso a período e coordenador.
- Validações de dados obrigatórios.
- Tela de listagem e filtro de cursos.

### Definition of Ready (DoR)
- Tabela `Curso`, `Periodo` e `Coordenador` definidas.
- Mockups de tela de cursos disponíveis.

### Definition of Done (DoD)
- CRUD implementado e testado.
- Validações de UI e backend OK.
- Documentação e changelog atualizados.

---

## 6. Cadastrar e gerenciar informações dos professores
**Como coordenador, eu quero cadastrar e gerenciar informações dos professores, para ter controle sobre os recursos humanos do curso.**

### Descrição
Permite CRUD de professores, associando-os às matérias que lecionam.

### Critérios de Aceitação
- CRUD completo de professores.
- Associação de professor a disciplinas.
- Campos obrigatórios.

### Definition of Ready (DoR)
- Tabela `Professor` criada.
- Relacionamento com `Materia` definido.

### Definition of Done (DoD)
- Implementação de tela e DAOs.
- Testes automáticos e manuais.
- Documentação atualizada.

---

## 7. Gerenciar horários de aula (50 minutos e intervalos)
**Como coordenador, eu quero gerenciar os horários de aula, considerando a padronização de 50 minutos e variações de intervalos, para adequar o calendário ao curso.**

### Descrição
Configurar a duração padrão dos slots e opções de intervalo entre aulas.

### Critérios de Aceitação
- Configuração global de duração de slot (padrão 50min).
- UI para ajustar as aulas.
- Aplicação da configuração às telas de alocação.

### Definition of Ready (DoR)
- Tabela `Slot` e campo `status` definidos.
- Decisão sobre parametrização de tempo tomada.

### Definition of Done (DoD)
- Testes verificando duração e espaçamento.
- Atualização de documentação e changelog.
