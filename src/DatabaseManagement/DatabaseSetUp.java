package DatabaseManagement;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetUp {

    private static void dropAllTables(Connection connection){
        try{
            Statement statement = connection.createStatement();
            String query = "drop table Kupione_produkty";
            statement.executeUpdate(query);
            query = "drop table Zamowienia";
            statement.executeUpdate(query);
            query = "drop table Naprawy";
            statement.executeUpdate(query);
            query = "drop table Osoby";
            statement.executeUpdate(query);
            query = "drop table Produkty";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt drop connection");
            e.printStackTrace();
        }
    }
    public static void setUpAllTables(Connection connection){
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE Produkty (\n" +
                    "ID_produktu int PRIMARY KEY,\n" +
                    "Nazwa varchar(255) UNIQUE NOT NULL,\n" +
                    "Material_wykonania varchar(255) NOT NULL,\n" +
                    "Ilosc int NOT NULL,\n" +
                    "Cena_za_naprawe decimal(10,2) NOT NULL,\n" +
                    "Typ_instrumentu varchar(255) NOT NULL,\n" +
                    "Opis varchar(255),\n" +
                    "Cena decimal(10,2) NOT NULL,\n" +
                    "Producent varchar(255) NOT NULL,\n" +
                    "Przeceny decimal(10,2)\n" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE Osoby (\n" +
                    "ID_osoby int PRIMARY KEY,\n" +
                    "Imie_i_nazwisko varchar(255) NOT NULL,\n" +
                    "Nr_telefonu int,\n" +
                    "Login varchar(255) NOT NULL,\n" +
                    "Haslo varchar(255) NOT NULL,\n" +
                    "Email varchar(255) NOT NULL,\n" +
                    "Adres varchar(255),\n" +
                    "Status varchar(10) NOT NULL,\n" +
                    "Wyplata decimal(10,2),\n" +
                    "Data_zatrudnienia date\n" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE Naprawy (\n" +
                    "ID_naprawy int PRIMARY KEY,\n" +
                    "ID_osoby int NOT NULL,\n" +
                    "Status_zamowienia varchar(15) NOT NULL,\n" +
                    "Data_zlozenia date NOT NULL,\n" +
                    "ID_produktu int NOT NULL,\n" +
                    "cena decimal(10,2) NOT NULL,\n" +
                    "FOREIGN KEY (ID_produktu) REFERENCES Produkty(ID_produktu),\n" +
                    "FOREIGN KEY (ID_osoby) REFERENCES Osoby(ID_osoby)\n" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE Zamowienia (\n" +
                    "ID_zamowienia int PRIMARY KEY,\n" +
                    "ID_osoby int NOT NULL,\n" +
                    "Status_zamowienia varchar(255) NOT NULL,\n" +
                    "Data_zlozenia date NOT NULL,\n" +
                    "Cena decimal(10,2) NOT NULL,\n" +
                    "FOREIGN KEY (ID_osoby) REFERENCES Osoby(ID_Osoby)\n" +
                    ");";
            statement.executeUpdate(query);
            query = "CREATE TABLE Kupione_produkty (\n" +
                    "ID_zamowienia int NOT NULL,\n" +
                    "Ilosc_danego_produktu int NOT NULL,\n" +
                    "ID_produktu int NOT NULL,\n" +
                    "FOREIGN KEY (ID_produktu) REFERENCES Produkty(ID_produktu),\n" +
                    "FOREIGN KEY (ID_zamowienia) REFERENCES Zamowienia(ID_zamowienia)\n" +
                    ");";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt set up tables");
            e.printStackTrace();
        }
    }
    private static void fillProdukty(Connection connection){
        try {
            Statement statement = connection.createStatement();
            String query = "insert into Produkty " +
                    "values(" +
                    //"id, nazwa, material, ilosc, cena za naprw, typ instr, opis, cena, firma, przecena
                    "1, 'Gitara', 'Dab', 10, 149.99, 'Strunowy', 'Ta gitara jest dobra', 499.99, 'Gibson', 0.00" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Produkty " +
                    "values(" +
                    //"id, nazwa, material, ilosc, cena za naprw, typ instr, opis, cena, firma, przecena
                    "2, 'Perkusja', 'Brzoza', 10, 800.00, 'Perkusyjny', 'Perkusja robi duze bum', 4999.99, 'Pearl', 0.00" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Produkty " +
                    "values(" +
                    //"id, nazwa, material, ilosc, cena za naprw, typ instr, opis, cena, firma, przecena
                    "3, 'Mikrofon', 'Metal', 20, 69.99, 'Elektroniczny', 'Nie zawiera strun glosowych', 149.99, 'Intel', 0.00" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Produkty " +
                    "values(" +
                    //"id, nazwa, material, ilosc, cena za naprw, typ instr, opis, cena, firma, przecena
                    "4, 'Tamburyn', 'Olow', 100, 19999.99, 'Perkusyjny', 'Uwaga! Nie jesc', 9999.99, 'Tencent', 0.00" +
                    ");";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt fill produkty table");
            e.printStackTrace();
        }
    }
    private static void fillOsoby(Connection connection){
        try {
            Statement statement = connection.createStatement();
            String query = "insert into Osoby " +
                    "values(" +
                    //id_osoby  imie_i_nazwisko   nr_telefonu    login    haslo    email  adres  status, wyplata, data_zatrudnienia
                    "1, 'Andrzej Nowak', 612846413, 'kierownik1', '1234', 'andrzejn@szef.pl', 'Karola Wojtyly 37', 'kierownik', 7000.08, TO_DATE('11.01.2024','DD.MM.YYYY')" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Osoby " +
                    "values(" +
                    //id_osoby  imie_i_nazwisko   nr_telefonu    login    haslo    email  adres  status wyplata data_zatrudnienia
                    "2, 'Adam Minski', 612846417, 'klient0', '1234', 'admin@tlen.pl', 'Jana Pawla II 21', 'klient', NULL, NULL" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Osoby " +
                    "values(" +
                    //id_osoby  imie_i_nazwisko   nr_telefonu    login    haslo    email  adres  status wyplata data_zatrudnienia
                    "3, 'Janina Kowalska', 845216537, 'klient1', '1234', 'jkowalska98@gmail.com', NULL, 'klient', NULL, NULL" +
                    ");";
            statement.executeUpdate(query);
            query = "insert into Osoby " +
                    "values(" +
                    //id_osoby  imie_i_nazwisko   nr_telefonu    login    haslo    email  adres  status wyplata data_zatrudnienia
                    "4, 'Ewa Bak', NULL, 'klient2', '1234', 'evafart@gmail.com', NULL, 'klient', NULL, NULL" +
                    ");";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt fill osoby table");
            e.printStackTrace();
        }
    }

    public static void setUpDatabase(Connection connection){
        try{
            dropAllTables(connection);
            setUpAllTables(connection);
            fillProdukty(connection);
            fillOsoby(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
