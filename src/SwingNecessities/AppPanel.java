package SwingNecessities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import DatabaseManagement.ClientFunctions;
import DatabaseManagement.DatabaseSetUp;

public class AppPanel extends JPanel {
    Connection connectionToDatabase;
    int currentView = 0;
    String[] currentUser = new String[10];
    JButton databaseResetButton = new JButton("Reset bazy");
    JButton databaseSetupButton = new JButton("Setup bazy");


    //LOGIN
    JTextField usernameInput = new JTextField();
    JPasswordField passwordInput = new JPasswordField();
    JButton loginButton = new JButton("Log in");
    JButton makeNewAccountButton = new JButton("Stworz nowe konto");
    JTextField makeNewAccountName = new JTextField();
    JTextField makeNewAccountPhoneNumber = new JTextField();
    JTextField makeNewAccountLogin = new JTextField();
    JTextField makeNewAccountPassword = new JTextField();
    JTextField makeNewAccountPasswordDoubleCheck = new JTextField();
    JTextField makeNewAccountEmail = new JTextField();
    JTextField makeNewAccountAdress = new JTextField();
    JButton createAccountButton = new JButton("Stworz konto");


    //PRODUCTS
    JTable productsTable = new JTable();
    JScrollPane productsScroll = new JScrollPane(productsTable);
    JButton updateProductsButton = new JButton("Odswiez produkty");



    //CART
    JTable cartTable = new JTable();
    JScrollPane cartScroll = new JScrollPane(cartTable);
    JButton addToCartButton = new JButton("Dodaj do koszyka");
    JButton cleanCartButton = new JButton("Wyczysc koszyk");
    JButton buyButton = new JButton("Zloz zamowienie");
    JTextField buyingInput = new JTextField();
    JTextField amountInput = new JTextField();



    //ZAMOWIENIA
    JTable zamowieniaTable = new JTable();
    JScrollPane zamowieniaScroll = new JScrollPane(zamowieniaTable);
    JButton updateZamowieniaButton = new JButton("Odswiez zamowienia");
    JTable produktyWZamowieniuTable = new JTable();
    JScrollPane produktyWZamowieniuScroll = new JScrollPane(produktyWZamowieniuTable);
    JButton updateProduktyWZamowieniuButton = new JButton("Wyswietl zakupy");
    JButton cancelZamowienieButton = new JButton("Usun zamowienie");
    JTextField iDZamowieniaInput = new JTextField();
    
    
    //MENAGER
    JTable usersTable = new JTable();
    JScrollPane usersScroll = new JScrollPane(usersTable);
    JButton updateUsersButton = new JButton("Odswiez uzytkownikow");
    JTable kupioneTable = new JTable();
    JScrollPane kupioneScroll = new JScrollPane(kupioneTable);
    JButton updateKupioneButton = new JButton("Odswiez kupione");
    JTextField inputIDOsobyKupna = new JTextField();
    JButton readyButton = new JButton("Oznacz jako gotowe");
    JTextField inputOrderMenager = new JTextField();
    JButton deleteZamowienieButton = new JButton("Usun zamowienie");
    JTextField inputIDProduktuMenager = new JTextField();
    JTextField inputNewAmountOfProductMenager = new JTextField();
    JButton setNewAmountButton = new JButton("Zmien liczbe produktow");
    JButton deleteProductButton = new JButton("Usun ten produkt");
    JButton addNewProductButton = new JButton("Dodaj nowy produkt");

    JTextField newProductName = new JTextField();
    JTextField newProductMaterial = new JTextField();
    JTextField newProductAmount = new JTextField();
    JTextField newProductFixPrice = new JTextField();
    JTextField newProductType = new JTextField();
    JTextField newProductDesc = new JTextField();
    JTextField newProductPrice = new JTextField();
    JTextField newProductProducer = new JTextField();
    JTextField newProductSale = new JTextField();


    //OVERALL
    public AppPanel(int width, int height){
        setUpDatabaseConnection();
        this.setPreferredSize(new Dimension(width,height));
        setUpLogin();
        setUpLoginButton();
        this.add(databaseResetButton);
        databaseResetButton.setVisible(true);
        databaseResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseSetUp.setUpDatabase(connectionToDatabase);
            }
        });
        this.add(databaseSetupButton);
        databaseSetupButton.setVisible(true);
        databaseSetupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseSetUp.setUpAllTables(connectionToDatabase);
            }
        });
    }
    private void changeView(int newView){
        if(newView == 0){
            System.out.println("Status nieznany");
            return;
        }
        if(currentView == 0){
            this.remove(loginButton);
            this.remove(usernameInput);
            this.remove(passwordInput);
            this.remove(makeNewAccountButton);
            this.remove(databaseResetButton);
            this.remove(databaseSetupButton);
        }
        if(currentView == -1){
            this.remove(makeNewAccountName);
            this.remove(makeNewAccountPhoneNumber);
            this.remove(makeNewAccountLogin);
            this.remove(makeNewAccountPassword);
            this.remove(makeNewAccountPasswordDoubleCheck);
            this.remove(makeNewAccountEmail);
            this.remove(makeNewAccountAdress);
            this.remove(createAccountButton);
        }
        currentView = newView;
        switch(newView){
            case(-1):
                setUpNewAccountInput();
                break;
            case(1):
                updateProductsButton();
                setUpTablesClient();
                setUpAddingToCart();
                setUpBuying();
                setUpZamowieniaButton();
                setUpCleanCartButton();
                setUpChoosingZamowienie();
                break;
            case(2):
                updateProductsButton();
                setUpTablesManager();
                setUpUpdateUsersButton();
                setUpUpateKupioneButton();
                setUpReadyButton();
                setUpDeleteButton();
                setUpMagazynManipulation();
                break;
        }
    }
    
    
    //LOGIN
    private void setUpLogin(){
        this.add(usernameInput);
        this.add(passwordInput);
        this.add(makeNewAccountButton);
        makeNewAccountButton.setVisible(true);
        makeNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeView(-1);
            }
        });

        usernameInput.setFocusable(true);
        usernameInput.setVisible(true);
        passwordInput.setFocusable(true);
        passwordInput.setVisible(true);
    }
    private void setUpLoginButton(){
        loginButton.setFocusable(true);
        loginButton.setVisible(true);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initiateLogin();
            }
        });
        this.add(loginButton);
    }
    private void initiateLogin(){
        String username = usernameInput.getText();
        String password = String.valueOf(passwordInput.getPassword());
        ArrayList<String[]> allOsoby = ClientFunctions.fetchOsoby(connectionToDatabase);
        for(int i = 0; i < allOsoby.size(); i++){
            if(allOsoby.get(i)[3].equals(username)){
                if(allOsoby.get(i)[4].equals(password)){
                    currentUser = allOsoby.get(i);
                    int newView = 0;
                    switch(allOsoby.get(i)[7]){
                        case("klient"):
                            newView = 1;
                            break;
                        case("kierownik"):
                            newView = 2;
                            break;
                        case("admin"):
                            newView = 2;
                            break;
                    }
                    changeView(newView);
                }
            }
        }
    }
    private void setUpNewAccountInput(){
        this.add(makeNewAccountName);
        makeNewAccountName.setVisible(true);
        this.add(makeNewAccountPhoneNumber);
        makeNewAccountPhoneNumber.setVisible(true);
        this.add(makeNewAccountLogin);
        makeNewAccountLogin.setVisible(true);
        this.add(makeNewAccountPassword);
        makeNewAccountPassword.setVisible(true);
        this.add(makeNewAccountPasswordDoubleCheck);
        makeNewAccountPasswordDoubleCheck.setVisible(true);
        this.add(makeNewAccountAdress);
        makeNewAccountAdress.setVisible(true);
        this.add(makeNewAccountEmail);
        makeNewAccountEmail.setVisible(true);
        
        this.add(createAccountButton);
        createAccountButton.setVisible(true);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
                //System.out.println("Account created");
            }
        });
    }
    private void createAccount(){
        if(!makeNewAccountPassword.getText().equals(makeNewAccountPasswordDoubleCheck.getText())){
            return;
        }
        if(makeNewAccountName.getText() != "" && makeNewAccountLogin.getText() != "" && makeNewAccountPassword.getText() != "" && makeNewAccountEmail.getText() != "" && makeNewAccountPhoneNumber.getText() != ""){
            int iD = ClientFunctions.createNewAccount(connectionToDatabase, makeNewAccountName.getText(), makeNewAccountPhoneNumber.getText(), makeNewAccountLogin.getText(), makeNewAccountPassword.getText(), makeNewAccountEmail.getText(), makeNewAccountAdress.getText());
            if(iD > 0) {
                ArrayList<String[]> allOsoby = ClientFunctions.fetchOsoby(connectionToDatabase);
                for(int i = 1; i<allOsoby.size(); i++){
                    if(iD == Integer.parseInt(allOsoby.get(i)[0])){
                        for(int j = 0; j<10; j++){
                            currentUser[j] = allOsoby.get(i)[j];
                        }
                    }
                }
                changeView(1);
            }
        }
    }
    
    
    //ZAMOWIENIA
    private void updateZamowienia(){
        ArrayList<String[]> allZamowienia = ClientFunctions.fetchZamowienia(connectionToDatabase);
        String[] columnNames = {"ID zamowienia", "Status zamowienia", "Data zlozenia", "Cena"};
        ArrayList<Object[]> userZamowienia = new ArrayList<>();
        for(int i = 1; i< allZamowienia.size(); i++){
            if(allZamowienia.get(i)[1].equals(currentUser[0])){
                Object[] toAdd = new Object[4];
                toAdd[0] = allZamowienia.get(i)[0];
                toAdd[1] = allZamowienia.get(i)[2];
                toAdd[2] = allZamowienia.get(i)[3];
                toAdd[3] = allZamowienia.get(i)[4];
                userZamowienia.add(toAdd);
            }
        }
        Object[][] data = new Object[userZamowienia.size()][4];
        for(int i = 0; i<userZamowienia.size(); i++){
            data[i][0] = userZamowienia.get(i)[0];
            data[i][1] = userZamowienia.get(i)[1];
            data[i][2] = userZamowienia.get(i)[2];
            data[i][3] = userZamowienia.get(i)[3];
        }
        JTable newZamowienia = new JTable(data, columnNames);
        zamowieniaTable = newZamowienia;
        this.remove(zamowieniaScroll);
        JScrollPane newScroll = new JScrollPane(zamowieniaTable);
        zamowieniaScroll = newScroll;
        this.add(zamowieniaScroll);
    }
    private void setUpZamowieniaButton(){
        this.add(updateZamowieniaButton);
        updateZamowieniaButton.setVisible(true);
        updateZamowieniaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateZamowienia();
            }
        });
    }
    private void setUpChoosingZamowienie(){
        this.add(iDZamowieniaInput);
        iDZamowieniaInput.setVisible(true);

        this.add(cancelZamowienieButton);
        cancelZamowienieButton.setVisible(true);
        cancelZamowienieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelZamowienie();
            }
        });

        this.add(updateProduktyWZamowieniuButton);
        updateProduktyWZamowieniuButton.setVisible(true);
        updateProduktyWZamowieniuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduktyWZamowieniu();
            }
        });
    }
    private void updateProduktyWZamowieniu(){
        String iDZamowienia = iDZamowieniaInput.getText();
        ArrayList<String[]> allKupioneProdukty = ClientFunctions.fetchKupioneProdukty(connectionToDatabase);
        String[] columnNames = {"Nazwa", "ilosc"};
        ArrayList<Object[]> usersProdukty = new ArrayList<>();
        for(int i = 1; i< allKupioneProdukty.size(); i++){
            if(allKupioneProdukty.get(i)[0].equals(iDZamowienia)){
                //weryfikacja czy to ta sama osoba co zlozyla zamowienie
                ArrayList<String[]> allZamowienia = ClientFunctions.fetchZamowienia(connectionToDatabase);
                for(int j = 0; j<allZamowienia.size(); j++){
                    if(allZamowienia.get(j)[0].equals(iDZamowienia) && allZamowienia.get(j)[1].equals(currentUser[0])){
                        Object[] toAdd = new Object[2];
                        toAdd[1] = allKupioneProdukty.get(i)[1];    //ilosc
                        toAdd[0] = allKupioneProdukty.get(i)[2];    //iD prod
                        usersProdukty.add(toAdd);
                    }
                }
            }
        }
        Object[][] data = new Object[usersProdukty.size()][2];
        for(int i = 0; i<usersProdukty.size(); i++){
            data[i][0] = usersProdukty.get(i)[0];   //idProd
            data[i][1] = usersProdukty.get(i)[1];   //ilosc
        }
        ArrayList<String[]> allProdukty = ClientFunctions.fetchProdukty(connectionToDatabase);
        for(int i = 0; i<allProdukty.size(); i++){
            for(int j = 0; j<usersProdukty.size(); j++){
                if(allProdukty.get(i)[0].equals(data[j][0])){
                    data[j][0] = allProdukty.get(i)[1];
                }
            }
        }
        JTable newProdukty = new JTable(data, columnNames);
        produktyWZamowieniuTable = newProdukty;
        this.remove(produktyWZamowieniuScroll);
        JScrollPane newScroll = new JScrollPane(produktyWZamowieniuTable);
        produktyWZamowieniuScroll = newScroll;
        this.add(produktyWZamowieniuScroll);
    }
    private void cancelZamowienie(){
        String iDZamowienia = iDZamowieniaInput.getText();
        ArrayList<String[]> allKupioneProdukty = ClientFunctions.fetchKupioneProdukty(connectionToDatabase);
        for(int i = 1; i< allKupioneProdukty.size(); i++){
            if(allKupioneProdukty.get(i)[0].equals(iDZamowienia)){
                //weryfikacja czy to ta sama osoba co zlozyla zamowienie
                ArrayList<String[]> allZamowienia = ClientFunctions.fetchZamowienia(connectionToDatabase);
                for(int j = 1; j<allZamowienia.size(); j++){
                    if(allZamowienia.get(j)[0].equals(iDZamowienia) && allZamowienia.get(j)[1].equals(currentUser[0])){
                        ClientFunctions.removeZamowienie(connectionToDatabase, Integer.parseInt(iDZamowienia));
                    }
                }
            }
        }
    }
    
    
    //CART
    private void cleanCart(){
        String[] columnNames = {"iD", "Nazwa", "Ilosc", "Cena"};
        Object[][] data = {};
        JTable cleanCart = new JTable(data, columnNames);
        cartTable = cleanCart;
        this.remove(cartScroll);
        JScrollPane newCartScroll = new JScrollPane(cartTable);
        cartScroll = newCartScroll;
        this.add(cartScroll);
    }
    private void setUpCleanCartButton(){
        this.add(cleanCartButton);
        cleanCartButton.setVisible(true);
        cleanCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanCart();
            }
        });
    }
    private void addProductToCart(int iDToFind, int amount){
        ArrayList<String[]> allProducts = ClientFunctions.fetchProdukty(connectionToDatabase);
        for(int i = 0; i< allProducts.size(); i++){
            if(Integer.parseInt(allProducts.get(i)[0]) == iDToFind){
                String[] columnNames = {"iD", "Nazwa", "Ilosc", "Cena"};
                ArrayList<Object[]> currentCart = new ArrayList<>();
                if(Integer.parseInt(allProducts.get(i)[3]) < amount){   //check czy nie zamawiamy wiecej niz jest w sklepie
                    return;
                }
                for(int j = 0; j<cartTable.getRowCount(); j++){
                    Object[] toAdd = new Object[4];
                    toAdd[0] = cartTable.getValueAt(j, 0);
                    toAdd[1] = cartTable.getValueAt(j, 1);
                    toAdd[2] = cartTable.getValueAt(j, 2);
                    toAdd[3] = cartTable.getValueAt(j, 3);
                    currentCart.add(toAdd);
                }
                Object[] toAdd = new Object[4];
                toAdd[0] = allProducts.get(i)[0];
                toAdd[1] = allProducts.get(i)[1];
                toAdd[2] = amount;
                toAdd[3] = Float.parseFloat(allProducts.get(i)[7]) * amount;
                currentCart.add(toAdd);
                Object[][] data = new Object[currentCart.size()][4];
                for(int j = 0; j<currentCart.size(); j++){
                    data[j][0] = currentCart.get(j)[0];
                    data[j][1] = currentCart.get(j)[1];
                    data[j][2] = currentCart.get(j)[2];
                    data[j][3] = currentCart.get(j)[3];
                }
                JTable newCart = new JTable(data, columnNames);
                cartTable = newCart;
                this.remove(cartScroll);
                JScrollPane newScroll = new JScrollPane(cartTable);
                cartScroll = newScroll;
                this.add(cartScroll);
                break;
            }
        }
    }
    private void setUpAddingToCart(){
        this.add(buyingInput);
        this.add(amountInput);
        this.add(addToCartButton);
        addToCartButton.setVisible(true);
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int productID = Integer.parseInt(buyingInput.getText());
                    int amount = Integer.parseInt(amountInput.getText());
                    addProductToCart(productID, amount);
                }catch(Exception f){
                    System.out.println("This is not a valid iD");
                }
            }
        });
        buyingInput.setVisible(true);
    }
    private void buyAllInCart(){
        ArrayList<Object[]> currentCart = new ArrayList<>();
        for(int j = 0; j<cartTable.getRowCount(); j++){
            Object[] toAdd = new Object[3];
            toAdd[0] = cartTable.getValueAt(j, 0);  //id
            toAdd[1] = cartTable.getValueAt(j, 2);  //ilosc
            toAdd[2] = cartTable.getValueAt(j, 3);  //cena
            currentCart.add(toAdd);
        }
        ClientFunctions.makeNewZamowienie(connectionToDatabase, currentUser, currentCart);
    }
    private void setUpBuying(){
        this.add(buyButton);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyAllInCart();
            }
        });
        buyButton.setVisible(true);
    }
    private void setUpDatabaseConnection(){
        connectionToDatabase = null;
        try {
            Class.forName("org.postgresql.Driver");
            connectionToDatabase = DriverManager.getConnection("jdbc:postgresql://localhost:5433/postgres", "MK_MM_Username", "MK_MM_Password");
            if (connectionToDatabase != null) {
                System.out.println("Connected to database");
            }
        }catch(Exception e){
            System.out.println("Couldn't connect to database");
        }
    }
    
    
    //PRODUKTY
    private void setUpTablesClient(){
        cartTable.setPreferredSize(new Dimension(200, 100));
        cartTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNames = {"iD", "Nazwa", "Ilosc", "Cena"};
        String[][] data = {};
        JTable temp = new JTable(data, columnNames);
        cartTable = temp;
        cartTable.setFocusable(false);
        cartTable.setVisible(true);
        cartScroll = new JScrollPane(cartTable);
        this.add(cartScroll);
        cartScroll.setFocusable(false);
        cartScroll.setVisible(true);


        zamowieniaTable.setPreferredSize(new Dimension(200, 100));
        zamowieniaTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNamesZamowienia = {"ID zamowienia", "Status zamowienia", "Data zlozenia", "Cena"};
        String[][] dataZamowienia = {};
        JTable temp1 = new JTable(dataZamowienia, columnNamesZamowienia);
        zamowieniaTable = temp1;
        zamowieniaTable.setFocusable(false);
        zamowieniaTable.setVisible(true);
        zamowieniaScroll = new JScrollPane(zamowieniaTable);
        this.add(zamowieniaScroll);
        zamowieniaScroll.setFocusable(false);
        zamowieniaScroll.setVisible(true);



        productsTable.setPreferredSize(new Dimension(800, 400));
        productsTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNamesProdukty = {"ID", "Nazwa", "Material", "Ilosc", "Cena za naprawe", "Typ", "Opis", "Cena", "Producent", "Przeceny"};
        String[][] dataProdukty = {};
        JTable temp2 = new JTable(dataProdukty, columnNamesProdukty);
        productsTable = temp2;
        productsTable.setFocusable(false);
        productsTable.setVisible(true);
        productsScroll = new JScrollPane(productsTable);
        this.add(productsScroll);
        productsScroll.setFocusable(false);
        productsScroll.setVisible(true);


        produktyWZamowieniuTable.setPreferredSize(new Dimension(800, 400));
        produktyWZamowieniuTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNamesProduktyWZamowieniu = {"Nazwa", "Ilosc"};
        String[][] dataProduktyWZamowieniu = {};
        JTable temp3 = new JTable(dataProduktyWZamowieniu, columnNamesProduktyWZamowieniu);
        produktyWZamowieniuTable = temp3;
        produktyWZamowieniuTable.setFocusable(false);
        produktyWZamowieniuTable.setVisible(true);
        produktyWZamowieniuScroll = new JScrollPane(produktyWZamowieniuTable);
        this.add(produktyWZamowieniuScroll);
        produktyWZamowieniuScroll.setFocusable(false);
        produktyWZamowieniuScroll.setVisible(true);
    }
    private void updateProductsTable(){
        ArrayList<String[]> allProducts = ClientFunctions.fetchProdukty(connectionToDatabase);
        String[] columnNames = {"ID", "Nazwa", "Material", "Ilosc", "Cena za naprawe", "Typ", "Opis", "Cena", "Producent", "Przeceny"};
        Object[][] data = new Object[allProducts.size()][10];
        for(int i = 0; i< allProducts.size(); i++) {
            for(int j = 0; j<10; j++){
                data[i][j] = allProducts.get(i)[j];
            }
        }
        JTable newTable = new JTable(data, columnNames);
        productsTable = newTable;
        this.remove(productsScroll);
        JScrollPane newScroll = new JScrollPane(productsTable);
        productsScroll = newScroll;
        this.add(productsScroll);
    }
    private void updateProductsButton(){
        this.add(updateProductsButton);
        updateProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductsTable();
            }
        });
        updateProductsButton.setVisible(true);
    }
    
    
    //MENAGER
    private void setUpTablesManager(){
        cartTable.setPreferredSize(new Dimension(200, 100));
        cartTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNames = {"iD", "Nazwa", "Ilosc", "Cena"};
        String[][] data = {};
        JTable temp = new JTable(data, columnNames);
        cartTable = temp;
        cartTable.setFocusable(false);
        cartTable.setVisible(true);
        cartScroll = new JScrollPane(cartTable);
        this.add(cartScroll);
        cartScroll.setFocusable(false);
        cartScroll.setVisible(true);

        usersTable.setPreferredSize(new Dimension(200, 100));
        usersTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNames1 = {"iD", "Imie i Nazwisko", "Nr telefonu", "login", "email", "status"};
        String[][] data1 = {};
        JTable temp1 = new JTable(data1, columnNames1);
        usersTable = temp1;
        usersTable.setFocusable(false);
        usersTable.setVisible(true);
        usersScroll = new JScrollPane(usersTable);
        this.add(usersScroll);
        usersScroll.setFocusable(false);
        usersScroll.setVisible(true);

        kupioneTable.setPreferredSize(new Dimension(200, 100));
        kupioneTable.setFont(new Font("Times New Roman", 0, 16));
        String[] columnNames2 = {"ID osoby", "iD zamowienia", "Nazwa", "Ilosc"};
        String[][] data2 = {};
        JTable temp2 = new JTable(data2, columnNames2);
        kupioneTable = temp2;
        kupioneTable.setFocusable(false);
        kupioneTable.setVisible(true);
        kupioneScroll = new JScrollPane(kupioneTable);
        this.add(kupioneScroll);
        kupioneScroll.setFocusable(false);
        kupioneScroll.setVisible(true);
    }
    private void setUpUpdateUsersButton(){
        this.add(updateUsersButton);
        updateUsersButton.setVisible(true);
        updateUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUsers();
            }
        });
    }
    private void updateUsers(){
        ArrayList<String[]> allUsers = ClientFunctions.fetchOsoby(connectionToDatabase);
        String[] columnNames = {"iD", "Imie i Nazwisko", "Nr telefonu", "login", "email", "status"};
        Object[][] data = new Object[allUsers.size()-1][6];
        for(int i = 1; i<allUsers.size(); i++){
            data[i-1][0] = allUsers.get(i)[0];
            data[i-1][1] = allUsers.get(i)[1];
            data[i-1][2] = allUsers.get(i)[2];
            data[i-1][3] = allUsers.get(i)[3];
            data[i-1][4] = allUsers.get(i)[5];
            data[i-1][5] = allUsers.get(i)[7];
        }
        JTable visibleUsers = new JTable(data, columnNames);
        usersTable = visibleUsers;
        this.remove(usersScroll);
        JScrollPane newScroll = new JScrollPane(usersTable);
        usersScroll = newScroll;
        this.add(usersScroll);
    }
    private void setUpUpateKupioneButton(){
        this.add(inputIDOsobyKupna);
        inputIDOsobyKupna.setVisible(true);

        this.add(updateKupioneButton);
        updateKupioneButton.setVisible(true);
        updateKupioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateKupione();
            }
        });
    }
    private void updateKupione(){
        String iDOsoby = inputIDOsobyKupna.getText();
        ArrayList<String[]> allKupioneProdukty = ClientFunctions.fetchKupioneProdukty(connectionToDatabase);
        ArrayList<String[]> allZamowienia = ClientFunctions.fetchZamowienia(connectionToDatabase);
        String[] columnNames = {"ID osoby", "iD zamowienia", "Nazwa", "Ilosc", "Status", "Data zlozenia", "Cena"};
        ArrayList<Object[]> usersProdukty = new ArrayList<>();
        for(int i = 1; i< allZamowienia.size(); i++){
            if(iDOsoby.equals("")){
                for(int j = 1; j<allKupioneProdukty.size(); j++){
                    if(allZamowienia.get(i)[0].equals(allKupioneProdukty.get(j)[0])) {
                        Object[] toAdd = new Object[7];
                        toAdd[0] = allZamowienia.get(i)[1];    //iD osoby
                        toAdd[1] = allKupioneProdukty.get(j)[0];    //iD zamowienia
                        toAdd[2] = allKupioneProdukty.get(j)[2];    //id produktu
                        toAdd[3] = allKupioneProdukty.get(j)[1];    //Ilosc
                        toAdd[4] = allZamowienia.get(i)[2];  //Status
                        toAdd[5] = allZamowienia.get(i)[3];  //data
                        toAdd[6] = allZamowienia.get(i)[4];  //cena
                        usersProdukty.add(toAdd);
                    }
                }
            }else{
                if(allZamowienia.get(i)[1].equals(iDOsoby)){    //czy to ta sama osoba
                    for(int j = 0; j<allKupioneProdukty.size(); j++){
                        if(allKupioneProdukty.get(j)[0].equals(allZamowienia.get(i)[0])){
                            Object[] toAdd = new Object[7];
                            toAdd[0] = allZamowienia.get(i)[1];    //iD osoby
                            toAdd[1] = allKupioneProdukty.get(j)[0];    //iD zamowienia
                            toAdd[2] = allKupioneProdukty.get(j)[2];    //id produktu
                            toAdd[3] = allKupioneProdukty.get(j)[1];    //Ilosc
                            toAdd[4] = allZamowienia.get(i)[2];  //Status
                            toAdd[5] = allZamowienia.get(i)[3];  //data
                            toAdd[6] = allZamowienia.get(i)[4];  //cena
                            usersProdukty.add(toAdd);
                        }
                    }
                }
            }
        }
        Object[][] data = new Object[usersProdukty.size()][7];
        for(int i = 0; i<usersProdukty.size(); i++){
            data[i][0] = usersProdukty.get(i)[0];   //id os
            data[i][1] = usersProdukty.get(i)[1];   //id zam
            data[i][2] = usersProdukty.get(i)[2];   //id prod
            data[i][3] = usersProdukty.get(i)[3];   //ilosc
            data[i][4] = usersProdukty.get(i)[4];   //Status
            data[i][5] = usersProdukty.get(i)[5];   //data
            data[i][6] = usersProdukty.get(i)[6];   //cena
        }
        ArrayList<String[]> allProdukty = ClientFunctions.fetchProdukty(connectionToDatabase);
        for(int i = 0; i<allProdukty.size(); i++){
            for(int j = 0; j<usersProdukty.size(); j++){
                if(allProdukty.get(i)[0].equals(data[j][2])){
                    data[j][2] = allProdukty.get(i)[1];
                }
            }
        }
        JTable newProdukty = new JTable(data, columnNames);
        kupioneTable = newProdukty;
        this.remove(kupioneScroll);
        JScrollPane newScroll = new JScrollPane(kupioneTable);
        kupioneScroll = newScroll;
        this.add(kupioneScroll);
    }
    private void setUpReadyButton(){
        this.add(inputOrderMenager);
        inputOrderMenager.setVisible(true);

        this.add(readyButton);
        readyButton.setVisible(true);
        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String iDZamowienia = inputOrderMenager.getText();
                ClientFunctions.setZamowienieAsReady(connectionToDatabase, Integer.parseInt(iDZamowienia));
            }
        });
    }
    private void setUpDeleteButton(){
        this.add(deleteZamowienieButton);
        deleteZamowienieButton.setVisible(true);
        deleteZamowienieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientFunctions.removeZamowienie(connectionToDatabase, Integer.parseInt(inputOrderMenager.getText()));
            }
        });
    }
    private void setUpMagazynManipulation(){
        this.add(inputIDProduktuMenager);
        inputIDProduktuMenager.setVisible(true);

        this.add(inputNewAmountOfProductMenager);
        inputNewAmountOfProductMenager.setVisible(true);

        this.add(setNewAmountButton);
        setNewAmountButton.setVisible(true);
        setNewAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientFunctions.setNewAmountOfProducts(connectionToDatabase, Integer.parseInt(inputIDProduktuMenager.getText()),
                        Integer.parseInt(inputNewAmountOfProductMenager.getText()));
            }
        });

        this.add(deleteProductButton);
        deleteProductButton.setVisible(true);
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientFunctions.deleteProduct(connectionToDatabase, Integer.parseInt(inputIDProduktuMenager.getText()));
            }
        });

        this.add(addNewProductButton);
        addNewProductButton.setVisible(true);
        addNewProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientFunctions.addNewProduct(connectionToDatabase, 
                        newProductName.getText(),
                        Integer.parseInt(newProductAmount.getText()),
                        newProductDesc.getText(),
                        Float.parseFloat(newProductPrice.getText()),
                        Float.parseFloat(newProductFixPrice.getText()),
                        newProductMaterial.getText(),
                        newProductProducer.getText(),
                        Float.parseFloat(newProductSale.getText()),
                        newProductType.getText()
                        );
            }
        });
        
        this.add(newProductName);
        newProductName.setVisible(true);
        this.add(newProductAmount);
        newProductAmount.setVisible(true);
        this.add(newProductDesc);
        newProductDesc.setVisible(true);
        this.add(newProductPrice);
        newProductPrice.setVisible(true);
        this.add(newProductFixPrice);
        newProductFixPrice.setVisible(true);
        this.add(newProductMaterial);
        newProductMaterial.setVisible(true);
        this.add(newProductProducer);
        newProductProducer.setVisible(true);
        this.add(newProductSale);
        newProductSale.setVisible(true);
        this.add(newProductType);
        newProductType.setVisible(true);
    }
    private void newProductPositions(Graphics g){
        newProductName.setBounds(10, 340, 50, 30);
        g.drawString("Nazwa", 60, 360);
        newProductAmount.setBounds(10, 380, 50, 30);
        g.drawString("Ilosc", 60, 400);
        newProductPrice.setBounds(10, 420, 50, 30);
        g.drawString("Cena", 60, 440);

        newProductFixPrice.setBounds(120, 340, 50, 30);
        g.drawString("Fix cena", 170, 360);
        newProductMaterial.setBounds(120, 380, 50, 30);
        g.drawString("Material", 170, 400);
        newProductProducer.setBounds(120, 420, 50, 30);
        g.drawString("Producent", 170, 440);

        newProductDesc.setBounds(230, 340, 80, 30);
        g.drawString("Opis", 310, 360);
        newProductSale.setBounds(230, 380, 50, 30);
        g.drawString("Przecena", 280, 400);
        newProductType.setBounds(230, 420, 50, 30);
        g.drawString("Typ", 280, 440);
    }


    //PAINT
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        switch(currentView){
            case(-1):
                makeNewAccountName.setBounds(500, 200, 200, 30);
                g.drawString("Imie i nazwisko:", 410, 220);
                makeNewAccountPhoneNumber.setBounds(500, 240, 200, 30);
                g.drawString("Numer telefonu: " , 410, 260);
                makeNewAccountLogin.setBounds(500, 280, 200, 30);
                g.drawString("Login: " , 463, 300);
                makeNewAccountPassword.setBounds(500, 320, 200, 30);
                g.drawString("Haslo: " , 460, 340);
                makeNewAccountPasswordDoubleCheck.setBounds(500, 360, 200, 30);
                g.drawString("Powtorz Haslo:" , 415, 380);
                makeNewAccountEmail.setBounds(500, 400, 200, 30);
                g.drawString("Email: " , 460, 420);
                makeNewAccountAdress.setBounds(500, 440, 200, 30);
                g.drawString("Adres*: " , 455, 460);
                createAccountButton.setBounds(500, 500, 200, 30);
                g.drawString("Pola z * nie sa wymagane " , 500, 570);
                break;
            case(0):
                usernameInput.setBounds(500, 345,200, 30);
                g.drawString("Login:", 460, 365);
                passwordInput.setBounds(500, 385, 200, 30);
                g.drawString("Password: ", 435, 405);
                loginButton.setBounds(500, 425, 200, 30);
                makeNewAccountButton.setBounds(800, 386, 200, 30);
                databaseResetButton.setBounds(10,10, 200, 30);
                databaseSetupButton.setBounds(10, 50, 200, 30);
                break;
            case(1):
                updateProductsButton.setBounds(10, 10, 180, 30);
                productsScroll.setBounds(10, 50, 800, 400);

                buyingInput.setBounds(820, 10, 20, 30);
                amountInput.setBounds(910, 10, 20, 30);
                addToCartButton.setBounds(820, 50, 200, 30);
                cartScroll.setBounds(820, 90, 200, 100);
                cleanCartButton.setBounds(820, 200, 200, 30);
                buyButton.setBounds(820, 240, 200, 30);

                updateZamowieniaButton.setBounds(10, 460, 200, 30);
                zamowieniaScroll.setBounds(10, 500, 400, 200);

                updateProduktyWZamowieniuButton.setBounds(450, 460,170, 30);
                iDZamowieniaInput.setBounds(420, 460, 20, 30);
                produktyWZamowieniuScroll.setBounds(420, 500, 200, 200);
                cancelZamowienieButton.setBounds(420, 710, 200, 30);

                g.drawString("iD", 845, 30);
                g.drawString("Ilosc", 935, 30);
                g.drawString("Zalogowano: "+ currentUser[1], 200, 30);
                break;
            case(2):
                updateProductsButton.setBounds(10, 10, 180, 30);
                productsScroll.setBounds(10, 50, 800, 160);

                inputIDProduktuMenager.setBounds(10, 220, 20, 30);
                g.drawString("iD Produktu", 30, 240);
                inputNewAmountOfProductMenager.setBounds(110, 220, 40, 30);
                g.drawString("Nowa ilosc", 150, 240);
                setNewAmountButton.setBounds(230, 220, 200, 30);
                deleteProductButton.setBounds(10, 260, 200, 30);


                addNewProductButton.setBounds(350, 380, 200, 30);
                newProductPositions(g);

                updateUsersButton.setBounds(10, 460, 200, 30);
                usersScroll.setBounds(10, 500, 800, 300);

                inputIDOsobyKupna.setBounds(820, 10, 20, 30);
                g.drawString("ID Osoby", 840, 30);
                updateKupioneButton.setBounds(900, 10, 200, 30);
                kupioneScroll.setBounds(820, 50 ,370, 100);
                inputOrderMenager.setBounds(820, 160, 20, 30);
                g.drawString("ID zamowienia", 840, 180);
                readyButton.setBounds(820, 200, 180, 30);
                deleteZamowienieButton.setBounds(1010, 200, 180, 30);
                break;
        }
    }
}
