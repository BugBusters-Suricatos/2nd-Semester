package org.example.gestaodehorario.model;

/**
 * Representa um coordenador acadêmico no sistema, contendo informações de identificação
 * e autenticação. Utilizado para gestão de acesso e operações privilegiadas.
 */
public class Coordenador {
    private Integer idCoordenador;
    private String nome;
    private String email;
    private String senha;

    /**
     * Construtor padrão que cria uma instância vazia de Coordenador
     */
    public Coordenador() {
    }

    /**
     * Construtor completo para criação de coordenador com todos os atributos
     * @param idCoordenador Identificador único do coordenador
     * @param nome Nome completo do coordenador
     * @param email Endereço de email institucional
     * @param senha Senha de acesso ao sistema (armazenada como texto simples - considerar criptografia)
     */
    public Coordenador(Integer idCoordenador, String nome, String email, String senha) {
        this.idCoordenador = idCoordenador;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    /**
     * @return ID único do coordenador
     */
    public Integer getIdCoordenador() {
        return idCoordenador;
    }

    /**
     * Define o ID do coordenador
     * @param idCoordenador Novo identificador único
     */
    public void setIdCoordenador(Integer idCoordenador) {
        this.idCoordenador = idCoordenador;
    }

    /**
     * @return Nome completo do coordenador
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do coordenador
     * @param nome Novo nome completo
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return Email institucional do coordenador
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do coordenador
     * @param email Novo endereço de email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Senha de acesso (texto não criptografado)
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha de acesso
     * @param senha Nova senha (deveria ser armazenada de forma segura)
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Representação textual simplificada do coordenador
     * @return Nome do coordenador (para exibição em interfaces)
     */
    @Override
    public String toString() {
        return nome;
    }
}