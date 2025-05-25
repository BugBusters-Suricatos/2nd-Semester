# 📝 Anotações Product Owner — Projeto Gerenciador de Horários de Aula

Documentação de interações, decisões, feedbacks e pontos levantados entre equipe de desenvolvimento e cliente durante a condução do projeto.

---

## 📋 Sumário

- [Propostas de Sprints e Entregas](#propostas-de-sprints-e-entregas)
- [Feedback do Cliente sobre Sprints e Cadastros](#feedback-do-cliente-sobre-sprints-e-cadastros)
- [Dúvidas de UX/UI e Funcionalidades Prioritárias](#dúvidas-de-uxui-e-funcionalidades-prioritárias)
- [Rotina Atual do Cliente](#rotina-atual-do-cliente)
- [Coleta de Material e Levantamento de Regras](#coleta-de-material-e-levantamento-de-regras)
- [Restrições Técnicas e Ambiente](#restrições-técnicas-e-ambiente)
- [Requisitos do Cadastro de Professores](#requisitos-do-cadastro-de-professores)
- [Exportação e Visualização](#exportação-e-visualização)
- [Feedbacks sobre Prototipação e Apresentação](#feedbacks-sobre-prototipação-e-apresentação)
- [Andamento e Últimos Feedbacks](#andamento-e-últimos-feedbacks)

---

## Propostas de Sprints e Entregas

**Cleber Kirch (Product Owner) — 18h53**
> Boa tarde Sr. @Mineda.  
> Para iniciarmos o desenvolvimento do aplicativo Gerenciador de Horários de Aula solicitado, baseado nos requisitos funcionais e não-funcionais solicitados, gostaríamos de propor a seguinte divisão de entregas:
>
> **SPRINT 1**
> - Cadastro e gestão dos cursos e disciplinas
> - Cadastro e gestão dos professores
> - Cadastro dos horários de aula disponíveis para o curso
>
> **SPRINT 2**
> - Gerenciamento do semestre letivo (informar a qual semestre a disciplina pertence)
> - Gerenciamento dos horários
> - Mecanismos para evitar sobreposição de horários
>
> **SPRINT 3**
> - Sugestões automáticas de alocação de acordo com horários definidos
> - Entrega de manuais, guia de instalação e modelos de entidade-relacionamento
>
> Podemos validar as entregas esperadas acima? Me coloco à disposição para uma reunião segunda-feira à noite para alinharmos estes pontos.
> Em caso de atrasos ou adiantamentos, manteremos o senhor informado.

---

## Feedback do Cliente sobre Sprints e Cadastros

**Mineda — 8h01**
> O cliente no geral não entende de Scrum, então discutir Sprints não seria nada produtivo.  
> Para a Sprint 1 esperamos que vocês entendam o problema, então não é necessário entregar um software funcional (apenas telas estaria bom).

**Mineda — 8h02**
> Cadastros em geral, como cursos, disciplinas, professores, horários, etc dificilmente mudam. Vocês podem já deixar cadastrado de acordo com o que temos hoje. Não é algo prioritário pra gente.

---

## Dúvidas de UX/UI e Funcionalidades Prioritárias

**Cleber Kirch — 20h14**
> Algumas dúvidas levantadas pela nossa equipe:
> 1. O aplicativo precisa ser tela cheia (fullscreen) ou se abrir uma janela já atenderia?
> 2. Qual a ordem das opções que você gostaria que o menu tivesse? Ex: Cadastros, Criar Aulas...
> 3. Gostaria de um menu tradicional na parte superior, ou que sejam tipo botões clicáveis na tela?
> 4. Os cadastros e gestão de aulas, gostaria que abra na mesma tela/janela, ou que abrisse uma tela separada?
> 5. Considerando que os cadastros não são prioritários, quais as funções que o senhor consideraria as mais importantes?

**Mineda — 20h44**
> 1. Não precisa ser tela cheia  
> 2. Podem colocar da forma que acharem melhor  
> 3. As coisas precisam ser fáceis de achar. Principalmente a parte de alocar disciplinas  
> 4. Tanto faz  
> 5. Alocar disciplinas

---

## Rotina Atual do Cliente

**Cleber Kirch — 20h58**
> O senhor poderia descrever brevemente como é feito hoje o procedimento na empresa? Poderia mandar um print de algumas telas do sistema/planilha atual? Com estas informações, poderemos entender como funciona o seu processo.

**Mineda — 21h24**
> Hoje eu tenho as disciplinas de cada semestre e quantas aulas por semana. Também tenho qual professor está associado a cada disciplina.

**Mineda — 21h30**
> Também tenho acesso às planilhas dos outros coordenadores. Ao alocar as disciplinas eu preciso olhar as minhas planilhas e a dos outros coordenadores para ver se não tem conflito de horários.

---

## Coleta de Material e Levantamento de Regras

**Cleber Kirch — 23h31**
> O senhor pode nos fornecer a planilha atual, preenchida com casos reais de outro semestre?  
> A planilha atual é primordial para verificarmos, por exemplo, que campos são esperados em cada cadastro, para que o software não perca nenhuma funcionalidade em relação ao que vocês já têm hoje.
>
> Algumas dúvidas sobre a regra de negócio:
> - Existe alguma regra específica de priorização ao alocar professores e disciplinas?
> - Há casos frequentes de ajustes após a alocação inicial? Se sim, o que os causa?
> - Além da alocação de disciplinas, há outras dificuldades no planejamento de horários que o sistema poderia ajudar a resolver?
> - Como você imagina a interface ideal para a alocação de disciplinas? Clicar e arrastar, tabela em grid "estilo excel"?
> - Você prefere que o sistema sugira automaticamente horários para evitar conflitos ou apenas alerte sobre sobreposições?
> - É necessário um controle de permissões para que apenas coordenadores específicos possam editar horários?
> - Existem professores com restrições de horário além da sobreposição (ex.: só podem dar aula à noite)?
> - Os horários de aula variam conforme o curso, semestre ou dia da semana?
> - Alguma funcionalidade solicitada pode ser deixada para as próximas atualizações?

**Mineda — 23h35**
> A planilha atual está no site da FATEC.

**Mineda — 23h36**
> Feedback: muito texto. Se a empresa precisar rolar a tela pra ler, não irá responder.

**Mineda — 23h42**
> 1. O coordenador tenta colocar aulas do mesmo professor o mais agrupadas possível, para que ele venha menos dias  
> 2. Não muda  
> 3. Não, os conflitos são o maior problema  
> 4. Podem propor. Se for mais fácil que o Excel está bom  
> 5. Sugestão seria bom, mas deve dar erro se eu tentar forçar e tiver conflito  
> 6. Acho que não  
> 7. Sim, isso ocorre. Os professores passam em um papel quais slots não podem  
> 8. Variam por curso e dia  
> 9. Sim, se não der tempo

---

## Restrições Técnicas e Ambiente

**Cleber Kirch — 20h40**
> A sua empresa terá acesso à internet para a comunicação dos dados, por exemplo se as informações estiverem na nuvem?

**Mineda — 21h15**
> A ideia seria colocar o banco em um servidor local.

---

## Requisitos do Cadastro de Professores

**Cleber Kirch — 16h43**
> É possível que o mesmo coordenador gerencie mais que um curso?  
> Que dados você considera obrigatórios no cadastro dos professores?

**Mineda — 6h37**
> Coordenador pode ter somente um curso.

**Mineda — 6h38**
> Dos professores basta o nome, a matrícula e o tipo de contrato (determinado ou indeterminado).

---

## Exportação e Visualização

**Cleber Kirch — 19h35**
> Dúvida: a grade depois de montada, precisará ser exportada para PDF por exemplo, ou basta a visualização no aplicativo?

**Mineda — 22h14**
> Não precisa exportar, mas se tiver a função não vou reclamar.

---

## Feedbacks sobre Prototipação e Apresentação

**Cleber Kirch — 21h19**
> Segue telas desenvolvidas que serão apresentadas na semana que vem.  
> Em anexo: Menu Principal, Cadastro de Cursos, Disciplinas e Professores e Gestão de Horários

**Cleber Kirch — 19h26**
> Segue documento de proposta de entrega: Proposta_Entrega_GestaoDeHorario.pdf

**Mineda — 23h22**
> Feedback de professor:  
> A ordem de apresentação das telas importa. Eu falaria primeiro da tela de horários, dando ênfase na funcionalidade de arrastar.  
> O documento ficou muito técnico.  
> As telas com textos curtos exaltando as principais funcionalidades e ganhos seriam mais interessantes.

---

## Andamento e Últimos Feedbacks

**Cleber Kirch — 12h40**
> Desenvolvemos o sistema de cadastro da indisponibilidade de horários e também o sistema de alocação automática funcional, que será reportado amanhã na documentação de entrega.  
> Dúvidas sobre o andamento do projeto e funcionalidades, estamos à disposição.

**Mineda — 19h06**
> O que eu tinha falado na apresentação é que seria melhor se falasse por que o professor está indisponível. Ele tem restrição ou está em outra aula (seria bom se falasse qual aula).
