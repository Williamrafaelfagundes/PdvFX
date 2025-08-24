/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdvfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author willi
 */
public class ConexaoBanco {

    private static final String URL = "jdbc:mysql://localhost:3306/bancodedados";
    private static final String USUARIO = "root";
    private static final String SENHA = "Willrafa2182182!";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados", e);
        }
    }

    public static void criarOuAtualizarTabela() {
        try (Connection conexao = getConnection(); Statement stmt = conexao.createStatement()) {

            // Cria a tabela se não existir
            String sqlCreate = "CREATE TABLE IF NOT EXISTS produto ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nome VARCHAR(255) NOT NULL"
                    + ")";
            stmt.execute(sqlCreate);

            // Adiciona a coluna valor_venda, se ainda não existir
            try {
                stmt.execute("ALTER TABLE produto ADD COLUMN valor_venda DOUBLE");
                System.out.println("Coluna 'valor_venda' criada.");
            } catch (SQLException e) {
                if (!e.getMessage().toLowerCase().contains("duplicate column")) {
                    throw e;
                }
            }

            // Adiciona a coluna estoque, se ainda não existir
            try {
                stmt.execute("ALTER TABLE produto ADD COLUMN estoque DOUBLE");
                System.out.println("Coluna 'estoque' criada.");
            } catch (SQLException e) {
                if (!e.getMessage().toLowerCase().contains("duplicate column")) {
                    throw e;
                }
            }

            System.out.println("Tabela 'produto' criada/atualizada com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar ou atualizar tabela: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        criarTabelaSeNaoExistir();

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            // Mostrar todas as pessoas do banco
            conectarNoBanco();

            System.out.println("\nO que deseja fazer?");
            System.out.println("Adicionar (add)");
            System.out.println("Editar (ed)");
            System.out.println("Excluir (ex)");
            System.out.println("Visualizar tabela (ver)");
            System.out.println("Sair (sair)");
            System.out.print("Digite sua opção: ");
            String resposta = scanner.nextLine().toLowerCase();

            switch (resposta) {
                case "add":
                    adicionarProduto();
                    break;
                case "ed":
                    editarProduto();
                    break;
                case "ex":
                    excluirProduto();
                    break;
                case "ver":
                    visualizarTabela();
                    break;
                case "sair":
                    continuar = false;
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }

        scanner.close();
    }

    private static void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS produto ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "nome VARCHAR(255) NOT NULL, "
                + "valor_venda DOUBLE, "
                + "estoque DOUBLE"
                + ")";

        try (Connection conexao = getConnection(); Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'produto' verificada/criada com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela 'produto': " + e.getMessage());
        }
    }

    private static void conectarNoBanco() {
        Connection conexao = null;
        try {
            conexao = getConnection();
            if (conexao != null) {
                System.out.println("\n=== Lista de Produtos no Banco ===");

                String sql = "SELECT id, nome, valor_venda, estoque FROM produto";
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    double valorVenda = rs.getDouble("valor_venda");
                    double estoque = rs.getDouble("estoque");

                    Produto produto = new Produto(id, nome, valorVenda, estoque);
                    produto.mostrarDados();
                }
                System.out.println("==================================");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar o banco: " + e.getMessage());
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void adicionarProduto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o nome do novo produto: ");
        String nome = scanner.nextLine();

        String sql = "INSERT INTO produto (nome) VALUES (?)";

        try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto adicionado com sucesso!");
            } else {
                System.out.println("Falha ao adicionar produto.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
        }
    }

    private static void editarProduto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do produto que deseja editar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpa o \n

        System.out.print("Digite o novo nome: ");
        String novoNome = scanner.nextLine();

        String sql = "UPDATE produto SET nome = ? WHERE id = ?";

        try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, novoNome);
            pstmt.setInt(2, id);

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Produto com ID informado não foi encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao editar produto: " + e.getMessage());
        }
    }

    private static void excluirProduto() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do produto que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // limpa o \n

        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conexao = getConnection(); java.sql.PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Produto excluída com sucesso!");
            } else {
                System.out.println("Produto com ID informado não foi encontrada.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir Produto: " + e.getMessage());
        }
    }

    private static void visualizarTabela() {
        Connection conexao = null;
        try {
            conexao = getConnection();
            if (conexao != null) {
                System.out.println("\n=== Lista de Produtos no Banco ===");

                String sql = "SELECT id, nome, valor_venda, estoque FROM produto";
                Statement stmt = conexao.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    double valorVenda = rs.getDouble("valor_venda");
                    double estoque = rs.getDouble("estoque");

                    Produto produto = new Produto(id, nome, valorVenda, estoque);
                    produto.mostrarDados();
                }

                System.out.println("==================================");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar o banco: " + e.getMessage());
        } finally {
            try {
                if (conexao != null && !conexao.isClosed()) {
                    conexao.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
