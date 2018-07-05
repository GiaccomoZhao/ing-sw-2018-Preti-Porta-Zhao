package porprezhas.view.fx;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static porprezhas.view.fx.gameScene.GuiSettings.*;

public class BackgroundMusicPlayer {
    private static List<MediaPlayer> backgroundMusicPlayList = new ArrayList<>();
    private static String currentPlayingMusicPath = null;
    private static int currentPlayingMusicIndex;


    /**
     * Play all the Music from the given path Randomly and Sequentially
     *
     * This static synchronized method is thread-safe,
     * this would not give performance problem because it is called around 3~4 times in different timing
     * so, never contemporaneously
     *
     * @param musicPath The path to the music folder, from the root path as resource
     * @return is play successful
     */
    public static synchronized boolean playRandomMusic(String musicPath) {
        // Do not stop and replay the music from the same folder,
        // just continue it
        if(null != currentPlayingMusicPath  &&  currentPlayingMusicPath.equals(musicPath)) {
            return true;
        }

        // Stop and Clear the old play list
        if( backgroundMusicPlayList != null  &&  !backgroundMusicPlayList.isEmpty()) {
            backgroundMusicPlayList.get(currentPlayingMusicIndex).stop();
            backgroundMusicPlayList.clear();
        }
        // and then we can do a refresh of files in the folder

        String jarDir = null;
        CodeSource codeSource = BackgroundMusicPlayer.class.getProtectionDomain().getCodeSource();
        try {
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            jarDir = jarFile.getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            jarDir = new File(BackgroundMusicPlayer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        // get resource path
        String resourcePath =  "";  // jarDir + "\\" + pathToMusic;   //BackgroundMusicPlayer.class.getResource("/" ).getPath();
//        resourcePath = resourcePath.substring(1, resourcePath.length());

        // Convert the file url in file path format
        resourcePath = resourcePath.replaceAll("%20", " ");
        if(bDebug) {
            System.out.println("searching musinc in path: " + resourcePath + musicPath );
        }

        // Open the music directory
        File dir = new File(resourcePath + musicPath);
        if (!dir.exists() && dir.isDirectory()) {
            System.err.println("Cannot find audio source directory: " + dir);
            return false;
        }

        // Filter all .mp3 files
        String[] musicFiles = dir.list((dir1, name) -> name.endsWith(".mp3"));

        boolean bJar = true;
        if(null == musicFiles) {

            bJar = false;

            System.out.println("Using " + (bJar ? "jar" : "intelliJ") + "method");

            // let's try again with intelliJ method
            resourcePath = BackgroundMusicPlayer.class.getResource("/" ).getPath();
            resourcePath = resourcePath.substring(1, resourcePath.length());

            // Convert the file url in file path format
            resourcePath = resourcePath.replaceAll("%20", " ");
            if(bDebug) {
                System.out.println("searching musinc in path: " + resourcePath + musicPath );
            }

            // Open the music directory
            dir = new File(resourcePath + musicPath);
            if (!dir.exists() && dir.isDirectory()) {
                System.err.println("Cannot find audio source directory: " + dir);
                return false;
            }

            // Filter all .mp3 files
            musicFiles = dir.list((dir1, name) -> name.endsWith(".mp3"));


            if(null == musicFiles) {
                System.err.println("An Error has occurred: Incorrect music directory!");
                return false;
            }
        } else
        if(musicFiles.length == 0) {
            System.err.println("0 .mp3 file found in : " + dir);
            return false;
        }

        if(bDebug) {
            System.out.println("Music list found:"); }

        // Create MediaPlayers from all music contained in folder
        for (int i = 0; i < musicFiles.length; i++) {
            if(bDebug) {
                System.out.println((i+1) + ". " + musicFiles[i]);
            }

            // Setup a new MediaPlayer
            MediaPlayer player = null;
            if(bJar) {
                // Convert the file url in correct format
                musicFiles[i] = dir + "\\" + (musicFiles[i]).replace("\\", "/").replaceAll(" ", "%20");
                System.out.println(musicFiles[i]);

                // get absolute path
                String absoluteDir = "";
                try {
                    absoluteDir = new File( musicFiles[i]).toURI().toURL().toString();
                    if (bDebug)
                        System.out.println(absoluteDir);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                player = new MediaPlayer(new Media(absoluteDir));

            } else {
                // Convert the file url in correct format
                musicFiles[i] = "file:///" + (dir + "//" + musicFiles[i]).replace("\\", "/").replaceAll(" ", "%20");
                System.out.println(musicFiles[i]);

                player = new MediaPlayer(new Media(musicFiles[i]));
            }

            MediaPlayer finalPlayer = player;
            player.setOnError(() ->
                    System.err.println("Media player error occurred: " + finalPlayer.getError()));

            // set: play background music randomly one after an other
            player.setOnEndOfMedia(() -> {
                currentPlayingMusicIndex = new Random().nextInt(backgroundMusicPlayList.size());
                backgroundMusicPlayList.get(currentPlayingMusicIndex).play();
            });
            // bind some sound property for user setting
            player.muteProperty().bindBidirectional(bMuteMusic);
            player.volumeProperty().bindBidirectional(musicVolume);

            backgroundMusicPlayList.add(player);
        }

        if (backgroundMusicPlayList.isEmpty()) {
            System.out.println("\t0 .mp3 audio file found in " + dir);
            return false;
        }

        // start the first music
        currentPlayingMusicIndex = new Random().nextInt(backgroundMusicPlayList.size());
        backgroundMusicPlayList.get(currentPlayingMusicIndex).play();

        // save current playing music's path
        // now that the we have verified the path is valid
        if( !musicPath.equals(currentPlayingMusicPath) ) {
            currentPlayingMusicPath = musicPath;
        }

        return true;
    }

}
