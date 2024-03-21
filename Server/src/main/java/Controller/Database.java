package Controller;

import Controller.Profile.Profile;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.ArrayList;

// Chemin de la base de données
public class Database {
    private final static String modelFolderPath = "src/main/java/Model/";
    private static Database databaseInstance;
    private static int databaseSize = 0;
    private static List<Profile> dataBaseList;

     // Constructeur privé pour garantir une instance unique de la base de données
    private Database() throws Exception{
        dataBaseList = new ArrayList<>();
        setDatabase();
    }

    // Méthode pour obtenir l'instance unique de la base de données
    public static Database getInstance() throws Exception{
        if (databaseInstance == null) {
            databaseInstance = new Database();
            System.out.println("Une Nouvelle Database");
        }
        return databaseInstance;
    }

    // Méthode privée pour initialiser la base de données à partir des fichiers présents dans le répertoire spécifié
    private static void setDatabase() throws Exception{
        Creator creator = Creator.getInstance();
        File file = new File(modelFolderPath);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        // Vérifie si le répertoire existe et s'il contient des fichiers
        if(files == null || file == null){
            throw new Exception("Pardon, dossier intouvable! ");
        }

        // Nombre de fichiers dans la base de données
        databaseSize = files.length;

        // Parcours des fichiers pour créer les profils correspondants
        for(int i = 0; i < databaseSize; i++){
            dataBaseList.add(creator.setProfile(modelFolderPath, files[i].getName()));
        }
        System.out.println(databaseSize);
    }

    // Méthode pour obtenir la taille de la base de données
    public int getSize(){
        return databaseSize;
    }

    // Méthode pour obtenir le chemin de la base de données
    public String getmodelFolderPath(){
        return this.modelFolderPath;
    }

    // Méthode pour obtenir un profil par son nom d'utilisateur
    public Profile getProfileByName(String accessKey, String username){

        if(username == ""){
            username = accessKey.substring(0, accessKey.indexOf("$"));
        }
        Profile profile = null;
        for(int i = 0; i < databaseSize; i++){
            if(username.equals(dataBaseList.get(i).getUsername())){
                profile = dataBaseList.get(i);
            }
        }
        return profile;
    }

    // Méthode pour obtenir un profil par sa clé de connexion
    public Profile getProfileByKey(String accessKey)throws Exception{
        Profile profile = null;
        for(int i = 0; i < databaseSize; i++){
            if(accessKey.equals(dataBaseList.get(i).getaccessKey())){
                profile = dataBaseList.get(i);
            }
        }
        if(profile == null){
            throw new Exception("Pardon, profil intouvable! ");
        }
        return profile;
    }


    // Méthode pour ajouter un profil à la base de données
    public void addProfile(String accessKey) throws Exception{
        Creator creator = Creator.getInstance();
        if(this.databaseSize>0){
            if(getProfileByName(accessKey, "") != null){
                throw new Exception("Pardon, Profil deja existant!");
            }else{
                Profile profile = creator.createProfile(this.modelFolderPath, accessKey);
                this.dataBaseList.add(profile);
                this.databaseSize++;
            }
        }else{
            Profile profile = creator.createProfile(this.modelFolderPath, accessKey);
            this.dataBaseList.add(profile);
            this.databaseSize++;
        }
    }

    // Méthode pour supprimer un profil de la base de données
    public void deleteProfile(String accessKey) throws Exception{
        Delete delete = Delete.getInstance();
        Profile profile = getProfileByName(accessKey, "");
        if(this.databaseSize > 0){
            if(profile != null){
                delete.deleteProfile(profile);
                this.dataBaseList.remove(profile);
                this.databaseSize--;
            }
            else{
                throw new Exception("Profil introuvable pour la suppression.");
            }
        }
        else{
            throw new Exception("Oops, la base de données est vide.");
        }
    }

    // Méthode pour créer un fichier de données pour un profil
    public void createDataFile(String accessKey) throws Exception{
        Creator creator = Creator.getInstance();
        Profile tmpProfile = getProfileByKey(accessKey);
        if(tmpProfile == null){
            throw new Exception("Profil inexistant!");
        }
        creator.createDataFile(tmpProfile);
    }

}