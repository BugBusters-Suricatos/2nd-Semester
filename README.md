# 🎓 2nd-Semester - FATEC SJC 🚀

## 🦾 Nome do Grupo: **Suricatos** 

<img src="https://github.com/BugBusters-Suricatos/CalculadoraCientifica/blob/main/Logo%20Suricatos.png">


# Gerenciador de Horários de Aula

<p align="center">
    <a href="#-equipe">Equipe</a> |
    <a href="#-contextualização">Contextualização</a> |
    <a href="#-requisitos-funcionais-e-não-funcionais">Requisitos</a> |
    <a href="#-product-backlog">Product Backlog</a> |
    <a href="#-sprints">Sprints</a>
</p>

---
<p align="center">
<img loading="lazy" src="http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge"/>
</p>

# Nossa Equipe :busts_in_silhouette:

| FUNÇÃO | NOME | REDES SOCIAIS | FOTO |
| --- | --- | --- | --- |
| Product Owner      | Cleber Kirch        | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)](https://linkedin.com) [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com) |
| Scrum Master       | Ed Wilson       | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)](https://linkedin.com) [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)](https://github.com) |
| Developer    | Thomas Heinrich         | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |
| Developer    | Pedro H. Mattos         | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src= "https://github.com/user-attachments/assets/8108bdb8-c9d3-473e-9800-da1286cc91e5" alt="Pedro" style="width:60px;height:60px;">
| Developer    | Kaua R.                 | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |
| Developer    | Kelvin                  | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src= "https://github.com/user-attachments/assets/78117193-cfaf-4264-9170-b8d7069aec67" alt="Kelvin" style="width:60px;height:60px;">
| Developer    | Bruna Camile            | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |
| Developer    | Jordan Mello            | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src= "https://github.com/user-attachments/assets/4e45e966-aaad-43b6-a942-6768111e3551" alt="Jordan" style="width:60px;height:60px;">
| Developer    | William Honda           | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |
| Developer    | Aguinaldo Junior        | [![Linkedin](https://img.shields.io/badge/Linkedin-blue?style=flat-square&logo=Linkedin&logoColor=white)]() [![GitHub](https://img.shields.io/badge/GitHub-111217?style=flat-square&logo=github&logoColor=white)]() |<p align="center"><img src= "https://github.com/user-attachments/assets/29fe9cac-0ca1-410d-a42e-e8b6daf94640" alt="Aguinaldo" style="width:60px;height:60px;">

 ## :dart: Contextualização

A organização de horários de aula é frequentemente complexa, especialmente ao lidar com professores compartilhados entre cursos e semestres. Este projeto visa criar uma aplicação que gerencie horários de forma otimizada, evitando conflitos e promovendo uma organização prática e eficiente.

---

## :page_facing_up: Requisitos Funcionais

1. **Definição de Horário de Aula para um Semestre**  
   O sistema deve permitir que os coordenadores definam os horários de aula para um semestre letivo completo. Cada disciplina possui uma quantidade obrigatória de aulas por semana. Por exemplo, a disciplina "Arquitetura e Modelagem de Banco de Dados" possui 4 aulas semanais, sendo necessário definir individualmente o horário de cada uma dessas aulas. O sistema deve garantir a flexibilidade para ajustes e alterações conforme necessário.

2. **Mecanismo para Evitar Sobreposição de Horários**  
   O sistema deve implementar um mecanismo automático que impeça a sobreposição de horários de aula para professores que lecionam em disciplinas diferentes. Caso um conflito de horários seja detectado, o sistema deve fornecer alertas e sugerir alternativas para resolver o problema, garantindo que nenhum professor tenha duas aulas marcadas para o mesmo horário.

3. **Sugestão de Alocação de Horários**  
   O sistema deve oferecer sugestões inteligentes de alocação de horários de aula, levando em consideração os horários já definidos e as disponibilidades dos professores. As sugestões devem ser baseadas em algoritmos que otimizem o uso do tempo e dos recursos, facilitando a tarefa dos coordenadores na criação do cronograma de aulas.

4. **Gerenciamento de Semestre Letivo**  
   O sistema deve permitir o gerenciamento completo do semestre letivo, incluindo a adição, remoção e modificação de disciplinas, bem como a visualização de todas as disciplinas que compõem o semestre.

5. **Gerenciamento de Cursos**  
   O sistema deve fornecer ferramentas para o gerenciamento de diferentes cursos oferecidos pela instituição, permitindo cadastrar, editar e associar disciplinas específicas a cada curso.

6. **Gerenciamento de Disciplinas**  
   O sistema deve possibilitar o cadastro e a atualização de disciplinas, a associação delas a cursos e semestres específicos, além de permitir a visualização da carga horária de cada disciplina.

7. **Gerenciamento de Professores**  
   O sistema deve permitir o cadastro e gerenciamento de informações dos professores, incluindo dados pessoais, disciplinas que lecionam e o cronograma de aulas.

8. **Gerenciamento de Horários**  
   O sistema deve gerenciar os horários de aula padronizados em 50 minutos, permitindo personalizações conforme necessidades específicas de cada curso.

---

## :page_with_curl: Requisitos Não Funcionais

1. **Manual do Usuário**  
   O sistema deve incluir um manual do usuário detalhado com instruções acessíveis no sistema e em PDF.

2. **Guia de Instalação**  
   O sistema deve fornecer um guia de instalação abrangente, cobrindo diferentes plataformas e dependências.

3. **Modelo Entidade-Relacionamento (ER)**  
   O sistema deve incluir um modelo ER claro e documentado, atualizado conforme mudanças no banco de dados.

4. **Desempenho e Escalabilidade**  
   O sistema deve lidar com alta carga de usuários simultâneos, sem degradação perceptível.

5. **Segurança da Informação**  
   O sistema deve implementar autenticação de usuários e garantir a integridade das informações armazenadas.

6. **Usabilidade e Interface do Usuário**  
   O sistema deve possuir uma interface intuitiva e responsiva.

7. **Documentação Técnica**  
   O sistema deve incluir documentação para desenvolvedores, cobrindo arquitetura, API e processos.

---

## :date: User Stories

1. Como coordenador, eu quero definir os horários de aula para um semestre, para organizar o calendário de aulas.
2. Como coordenador, eu quero evitar sobreposição de horários, para garantir que os professores não tenham conflitos de horários.
3. Como coordenador, eu quero receber sugestões de alocação de horários, para otimizar a definição do calendário de aulas.
4. Como coordenador, eu quero gerenciar as disciplinas de cada semestre, para organizar o cronograma do curso.
5. Como coordenador, eu quero cadastrar e gerenciar cursos e disciplinas, para manter o sistema atualizado.
6. Como coordenador, eu quero cadastrar e gerenciar informações dos professores, para ter controle sobre os recursos humanos do curso.
7. Como coordenador, eu quero gerenciar os horários de aula, considerando a padronização de 50 minutos e variações de intervalos, para adequar o calendário ao curso.

---

## :star: Product Backlog

| **Rank** | **Prioridade** | **User Story**                                                                                   | **Estimativa** | **Sprint** |
|----------|----------------|---------------------------------------------------------------------------------------------------|----------------|------------|
| 1        | Alta           | Como coordenador, quero gerenciar cursos, disciplinas e professores para adicionar, editar e remover informações. | 5              | 1          |
| 2        | Alta           | Como coordenador, quero definir horários de aula para inserir e visualizar horários por disciplina e professor. | 5              | 1          |
| 3        | Alta           | Como coordenador, quero evitar a sobreposição de horários para que professores não tenham conflitos de horário. | 8              | 1          |
| 4        | Média          | Como coordenador, quero gerenciar semestres letivos para definir quais disciplinas fazem parte do semestre de um curso. | 5              | 2          |
| 5        | Média          | Como coordenador, quero um mecanismo de sugestão de alocação para otimizar a definição de horários de aula. | 8              | 2          |
| 6        | Média          | Como usuário, quero um manual detalhado para entender como usar todas as funções do aplicativo.  | 5              | 3          |
| 7        | Baixa          | Como usuário, quero um guia de instalação para instalar o aplicativo corretamente.               | 5              | 3          |
| 8        | Baixa          | Como desenvolvedor, quero um modelo de entidade relacionamento para garantir a correta implementação do banco de dados. | 8              | 3          |


## :calendar:  Sprints


| **SPRINTS** | **PERÍODOS**         | **DESCRIÇÃO**                                                      |
|-------------|----------------------|--------------------------------------------------------------------|
| Sprint 1    | 10/03/2025 à 30/03/2025 | Levantamento de requisitos, modelagem inicial, prototipagem, telas de cadastro      |
| Sprint 2    | 07/04/2025 à 27/04/2025 | Funções avançadas e checagem de conflitos|
| Sprint 3    | 05/05/2025 à 25/05/2025 | Sugestão de Alocação, Finalização, testes e elaboração de documentação.                  |

---
🔥 Vamos com tudo! #GoSuricatos 🚀  
