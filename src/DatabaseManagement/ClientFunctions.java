package DatabaseManagement;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ClientFunctions {
    public static ArrayList<String[]> fetchProdukty(Connection connection){
        try{
            ArrayList<String[]> allProducts = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "select * from Produkty";
            ResultSet rs = statement.executeQuery(query);
            String toAdd[] = new String[10];
            while(rs.next()) {
                for(int i = 0; i<10; i++){
                    toAdd[i] = rs.getString(i+1);;
                }
                allProducts.add(toAdd.clone());
            }
            return allProducts;
        }catch(Exception e){
            System.out.println("There has been an issue fetching data from the database");
        }
        return null;
    }
    public static ArrayList<String[]> fetchOsoby(Connection connection){
        try{
            ArrayList<String[]> allOsoby = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "select * from Osoby";
            ResultSet rs = statement.executeQuery(query);
            String toAdd[] = {"iD Osoby", "Imie i nazwisko", "nr Telefonu", "Login", "Haslo", "email", "Adres", "Status", "Wyplata", "Data zatrudnienia"};
            allOsoby.add(toAdd.clone());
            while(rs.next()) {
                for(int i = 0; i<10; i++){
                    toAdd[i] = rs.getString(i+1);;
                }
                allOsoby.add(toAdd.clone());
            }
            return allOsoby;
        }catch(Exception e){
            System.out.println("There has been an issue fetching data from the database");
        }
        return null;
    }
    public static ArrayList<String[]> fetchZamowienia(Connection connection){
        try{
            ArrayList<String[]> allZamowienia = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "select * from Zamowienia";
            ResultSet rs = statement.executeQuery(query);
            String toAdd[] = {"iD Zamowienia", "iD Osoby", "Status", "Data", "Cena"};
            allZamowienia.add(toAdd.clone());
            while(rs.next()) {
                for(int i = 0; i<5; i++){
                    toAdd[i] = rs.getString(i+1);;
                }
                allZamowienia.add(toAdd.clone());
            }
            return allZamowienia;
        }catch(Exception e){
            System.out.println("There has been an issue fetching data from the database");
        }
        return null;
    }
    public static ArrayList<String[]> fetchKupioneProdukty(Connection connection){
        try{
            ArrayList<String[]> allProdukty = new ArrayList<>();
            Statement statement = connection.createStatement();
            String query = "select * from Kupione_produkty";
            ResultSet rs = statement.executeQuery(query);
            String toAdd[] = {"ID zamowienia", "ilosc", "ID Produktu"};
            allProdukty.add(toAdd.clone());
            while(rs.next()) {
                for(int i = 0; i<3; i++){
                    toAdd[i] = rs.getString(i+1);;
                }
                allProdukty.add(toAdd.clone());
            }
            return allProdukty;
        }catch(Exception e){
            System.out.println("There has been an issue fetching data from the database");
        }
        return null;
    }
    private static void decreaseAmountInProdukty(ArrayList<Object[]> cart, Connection connection){
        ArrayList<String[]> allProdukty = fetchProdukty(connection);
        for(int i = 0; i<cart.size(); i++){
            for(int j = 0; j<allProdukty.size(); j++){
                if(Integer.parseInt(cart.get(i)[0].toString()) == Integer.parseInt(allProdukty.get(j)[0])){
                    int newAmount = Integer.parseInt(allProdukty.get(j)[3]) - Integer.parseInt(cart.get(i)[1].toString());
                    String query = "update Produkty set Ilosc = " + newAmount + " where ID_produktu = " +  allProdukty.get(j)[0];
                    try{
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(query);
                    }catch(Exception e){
                        System.out.println("Couldnt update Produkty");
                    }
                }
            }
        }
    }
    public static void makeNewZamowienie(Connection connection, String[] user, ArrayList<Object[]> cart){
        try {
            //ustalenie ceny
            float cena = 0;
            for(int i = 0; i<cart.size(); i++){
                cart.get(i)[2] = cart.get(i)[2] + "f";
                float cenadoDodania = Float.parseFloat(cart.get(i)[2].toString());
                cena += cenadoDodania;
            }
            //ustalenie daty
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String currentDateTime = dateFormat.format(currentDate);
            //ustalenie niezajetego id
            int iD = 1;
            ArrayList<String[]> zamowienia = fetchZamowienia(connection);
            for(int i = 0; i<zamowienia.size(); i++){
                for(int j = 1; j< zamowienia.size(); j++){
                    if(iD == Integer.parseInt(zamowienia.get(j)[0])){
                        iD++;
                    }else{
                        break;
                    }
                }
            }
            Statement statement = connection.createStatement();
            String query = "insert into Zamowienia " +
                    "values(" +
                    //ID zam, ID osob, Status, data zlozenia, cena,
                    iD + ", " + user[0] + ", 'Zamowiono', TO_DATE('" + currentDateTime +"','DD.MM.YYYY'), " + cena +
                    ");";
            statement.executeUpdate(query);
            decreaseAmountInProdukty(cart, connection);
            for(int i = 0; i<cart.size(); i++){
                query = "insert into Kupione_produkty values(" +
                        //id zamowienia, ilosc, id produktu
                        iD + ", " + cart.get(i)[1] + ", " + cart.get(i)[0] +
                        ");";
                statement.executeUpdate(query);
            }
        }catch(Exception e){
            System.out.println("Couldnt connect to database");
            e.printStackTrace();
        }
    }
    public static void removeZamowienie(Connection connection, int iDZamowienia){
        try{
            Statement statement = connection.createStatement();
            String query = "delete from Kupione_produkty where ID_zamowienia = " + iDZamowienia + ";";
            statement.executeUpdate(query);
            query = "delete from Zamowienia where ID_zamowienia = " + iDZamowienia + ";";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt remove order");
            e.printStackTrace();
        }

    }
    public static void setZamowienieAsReady(Connection connection, int iDZamowienia){
        try{
            //ArrayList<String[]> allZamowienia = fetchZamowienia(connection);
            Statement statement = connection.createStatement();
            String query = "update Zamowienia set Status_zamowienia = 'Gotowe' where ID_zamowienia = " + iDZamowienia + ";";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt update zamowienie");
            e.printStackTrace();
        }
    }
    public static void setNewAmountOfProducts(Connection connection, int iDProduktu, int newAmount){
        try {
            Statement statement = connection.createStatement();
            String query = "update Produkty set Ilosc = " + newAmount + " where ID_produktu = " + iDProduktu + ";";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt change product amount");
            e.printStackTrace();
        }
    }
    public static void deleteProduct(Connection connection, int iDProduktu){
        try {
            Statement statement = connection.createStatement();
            String query = "delete from Produkty where ID_produktu = " + iDProduktu + ";";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt delete product. Make sure it doesn't appear in any orders");
            //e.printStackTrace();
        }
    }
    public static void addNewProduct(Connection connection, String name, int amount, String desc, float price, float fixPrice, String material, String producer, float sale, String type){
        //ustalenie niezajetego id
        int iD = 1;
        ArrayList<String[]> produkty = fetchProdukty(connection);
        for(int i = 0; i<produkty.size(); i++){
            for(int j = 0; j< produkty.size(); j++){
                if(iD == Integer.parseInt(produkty.get(j)[0])){
                    iD++;
                }
            }
        }
        try {
            Statement statement = connection.createStatement();
            String query = "insert into Produkty " +
                    "values(" +
                    //"id, nazwa, material, ilosc, cena za naprw, typ instr, opis, cena, firma, przecena
                    iD + ", '" + name + "', '" + material + "', " + amount + ", " + fixPrice + ", '" + type + "', '" + desc + "', " + price + ", '" + producer + "', " + sale +
                    ");";
            statement.executeUpdate(query);
        }catch(Exception e){
            System.out.println("Couldnt insert new product");
            e.printStackTrace();
        }
    }
    public static int createNewAccount(Connection connection, String name, String phonenumber, String login, String password, String email, String address){
        //ustalenie nieistniejacego iD
        int iD = 1;
        ArrayList<String[]> osoby = fetchOsoby(connection);
        for(int i = 0; i<osoby.size(); i++){
            for(int j = 1; j< osoby.size(); j++){
                if(iD == Integer.parseInt(osoby.get(j)[0])){
                    iD++;
                }
            }
        }
        try {
            Statement statement = connection.createStatement();
            String query = "insert into Osoby " +
                    "values(" +
                    //id_osoby  imie_i_nazwisko   nr_telefonu    login    haslo    email  adres  status wyplata data_zatrudnienia
                    iD + ", '" + name + "', '" + phonenumber + "', '" + login + "', '" + password + "', '" + email + "', '" + address + "', 'klient', NULL, NULL" +
                    ");";
            statement.executeUpdate(query);
            return iD;
        }catch(Exception e){
            System.out.println("Couldn't create new account");
            e.printStackTrace();
            return -1;
        }
    }
}
