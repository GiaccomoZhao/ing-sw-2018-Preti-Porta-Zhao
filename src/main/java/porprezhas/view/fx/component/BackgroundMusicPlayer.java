package porprezhas.view.fx.component;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static porprezhas.view.fx.GuiSettings.*;

public class BackgroundMusicPlayer {
    private static List<MediaPlayer> backgroundMusicPlayers = new ArrayList<>();

    public static boolean playMusic() {
        // Clear the old list to ensure  not having duplication.
        // And then we can do a refresh of files in the folder
        backgroundMusicPlayers.clear();

        // get resource path
        String resourcePath = BackgroundMusicPlayer.class.getResource("/" ).getPath();
        resourcePath = resourcePath.substring(1, resourcePath.length());
        if(bDebug) {
            System.out.println("music resource path = " + resourcePath + pathToMusic );
        }

        // Open the music directory
        final File dir = new File(resourcePath + pathToMusic);
        if (!dir.exists() && dir.isDirectory()) {
            System.err.println("Cannot find audio source directory: " + dir);
            return false;
        }

        // Filter all .mp3 files
        String[] musicFiles = dir.list((dir1, name) -> name.endsWith(".mp3"));
        if(musicFiles.length == 0) {
            System.err.println("0 .mp3 file found in : " + dir);
            return false;
        }

        if(bDebug) {
            System.out.println(".MP3 Music list found:"); }

        // Create MediaPlayers from all music contained in folder
        for (int i = 0; i < musicFiles.length; i++) {
            if(bDebug) {
                System.out.println(i + ". " + musicFiles[i]); }

            // Convert the file url in correct format
            musicFiles[i] = "file:///" + (dir + "\\" + musicFiles[i]).replace("\\", "/").replaceAll(" ", "%20");

            // Setup a new MediaPlayer
            MediaPlayer player = new MediaPlayer( new Media(musicFiles[i]) );
            player.setOnError(() ->
                    System.err.println("Media player error occurred: " + player.getError()));
            backgroundMusicPlayers.add(player);
        }

        if (backgroundMusicPlayers.isEmpty()) {
            System.out.println("\t0 .mp3 audio file found in " + dir);
            return false;
        }

        // play background music randomly
        for (MediaPlayer player : backgroundMusicPlayers) {
            player.setOnEndOfMedia(() -> {
                backgroundMusicPlayers.get(new Random().nextInt(backgroundMusicPlayers.size())).play();
            });
            // bind some sound property for user setting
            player.muteProperty().bindBidirectional(bMuteMusic);
            player.volumeProperty().bindBidirectional(musicVolume);
        }

        // start the first music
        backgroundMusicPlayers.get(new Random().nextInt(backgroundMusicPlayers.size())).play();
        return true;
    }
}
