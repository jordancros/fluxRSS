/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modele;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jow
 */
public class Persistance 
{    
    private static Persistance instance = null;
    final static String jdbcURL = "jdbc:mysql://localhost:3306/news";
    final static String jdbcDriver = "com.mysql.jdbc.Driver";
    private Connection connexion=null;
    private Statement statement=null;
    
    private Persistance() throws SQLException,Exception
    {
        Class.forName(jdbcDriver);
        connexion = DriverManager.getConnection(jdbcURL, "root", "");
        statement = connexion.createStatement();
    }
    
    public static Persistance getInstance() throws SQLException, Exception
    {
        if(instance == null)
        {
            instance = new Persistance();
        }
        return instance;
    }
    
    public ResultSet lecture(String requete) throws SQLException, Exception
    {   
        ResultSet resultat = statement.executeQuery(requete);
        return resultat;
    }
    
    public void ajout(String requete) throws SQLException, Exception
    {
        statement.execute(requete);
    }
    
    public void suppression(String requete) throws SQLException, Exception
    {
        int modification = statement.executeUpdate(requete);
    }

    public void finalize() throws Exception, Throwable
    {
        super.finalize();
        statement.close();
        connexion.close();
    }
    
}