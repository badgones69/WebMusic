package controllers.playlist;

import db.PlaylistDb;
import db.MusiqueDb;
import dto.PlaylistDto;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import listeners.ListPlaylistSelectionListener;
import mapper.PlaylistMapper;
import mapper.MusiqueMapper;
import utils.ListenUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListenPlaylistController implements Initializable {

    // CURRENT MEDIA PLAYER
    private MediaPlayer mediaPlayer;
    // "PLAY" ICON
    private Image playButtonImage = new Image(getClass().getResourceAsStream("/icons/playing.png"));
    // "PAUSE" ICON
    private Image pauseButtonImage = new Image(getClass().getResourceAsStream("/icons/pause.png"));
    // CURRENT MEDIA PLAYER'S TOTAL LENGTH
    private Duration totalLength;

    /**
     * ALBUM LISTENING COMPONENTS
     */

    @FXML
    private MediaView playlistListeningMediaView = new MediaView();

    @FXML
    private ImageView playPauseImageView = new ImageView();

    @FXML
    private Label listeningPlaylistLabel = new Label();

    @FXML
    private Label listeningMusicTitleLabel = new Label();

    @FXML
    private Label listeningMusicArtistsLabel = new Label();

    @FXML
    private Label listeningCurrentTimeLabel = new Label();

    @FXML
    private Slider listeningProgressionSlider = new Slider();

    @FXML
    private Label listeningTotalLengthLabel = new Label();

    // GETTER
    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    /**
     * ALBUM LISTENING COMPONENTS INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InvalidationListener listeningProgressionSliderListener;

        PlaylistDto playlistDto = ListPlaylistSelectionListener.getPlaylistSelected();
        PlaylistDb playlistDb = PlaylistMapper.toDb(playlistDto);
        List<MusiqueDb> musiquesPlaylist = playlistDb.getListeMusiques();
        List<MediaPlayer> playlistTracks = new ArrayList<>();

        this.listeningPlaylistLabel.setText(playlistDb.getIntitulePlaylist());

        // MEDIA PLAYERS
        for (MusiqueDb musique : musiquesPlaylist) {
            playlistTracks.add(new MediaPlayer(new Media(new File(musique.getNomFichierMusique()).toURI().toString())));
        }

        listeningProgressionSliderListener = observable -> {
            double newTime = (listeningProgressionSlider.getValue() * totalLength.toMillis()) / 100;
            mediaPlayer.seek(new Duration(newTime));
        };

        InvalidationListener finalListeningProgressionSliderListener = listeningProgressionSliderListener;

        ChangeListener<Duration> mediaPlayerListener = (observable, oldValue, newValue) -> {
            listeningProgressionSlider.valueProperty().removeListener(finalListeningProgressionSliderListener);
            String progressionTimesFormatted = ListenUtils.formatTime(newValue, totalLength);
            listeningCurrentTimeLabel.setText(progressionTimesFormatted.substring(0, progressionTimesFormatted.indexOf('/')));
            listeningProgressionSlider.setDisable(totalLength.isUnknown());
            listeningProgressionSlider.setValue(newValue.toSeconds() / totalLength.toSeconds() * 100.0);
            if (!listeningProgressionSlider.isDisabled() && totalLength.greaterThan(Duration.ZERO) && !listeningProgressionSlider.isValueChanging()) {
                listeningProgressionSlider.setValue(newValue.toMillis() / (totalLength.toMillis()) * 100.0);
            }
            listeningProgressionSlider.valueProperty().addListener(finalListeningProgressionSliderListener);
        };

        // FIRST MUSIC INITIALIZATION
        this.playPauseImageView.setImage(this.playButtonImage);
        this.mediaPlayer = playlistTracks.get(0);
        this.mediaPlayer.setOnReady(() -> totalLength = this.mediaPlayer.getMedia().getDuration());
        this.mediaPlayer.currentTimeProperty().addListener(mediaPlayerListener);
        this.playlistListeningMediaView.setMediaPlayer(this.mediaPlayer);
        this.listeningMusicTitleLabel.setText(musiquesPlaylist.get(0).getTitreMusique());
        this.listeningProgressionSlider.setValue(Duration.ZERO.toSeconds());
        this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesPlaylist.get(0)).getAuteurs());
        this.listeningCurrentTimeLabel.setText("00:00");
        this.listeningTotalLengthLabel.setText(musiquesPlaylist.get(0).getDureeMusique().substring(3));

        // MUSICS PLAYING LINKING PREPARATION
        for (int i = 0; i < playlistTracks.size(); i++) {
            MediaPlayer finalMediaPlayer = playlistTracks.get(i);
            MediaPlayer nextMediaPlayer;
            if (i == playlistTracks.size()-1) {
                nextMediaPlayer = playlistTracks.get(0);
            } else {
                nextMediaPlayer = playlistTracks.get(i + 1);
            }

            MediaPlayer finalNextMediaPlayer = nextMediaPlayer;
            int finalI = i;

            finalMediaPlayer.setOnEndOfMedia(() -> {
                finalMediaPlayer.currentTimeProperty().removeListener(mediaPlayerListener);
                this.mediaPlayer = finalNextMediaPlayer;
                totalLength = this.mediaPlayer.getMedia().getDuration();
                this.mediaPlayer.currentTimeProperty().addListener(mediaPlayerListener);
                this.playPauseImageView.setImage(this.pauseButtonImage);

                if (finalI == playlistTracks.size()-1) {
                    this.listeningMusicTitleLabel.setText(musiquesPlaylist.get(0).getTitreMusique());
                    this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesPlaylist.get(0)).getAuteurs());
                    this.listeningTotalLengthLabel.setText(musiquesPlaylist.get(0).getDureeMusique().substring(3));
                } else {
                    this.listeningMusicTitleLabel.setText(musiquesPlaylist.get(finalI+1).getTitreMusique());
                    this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesPlaylist.get(finalI+1)).getAuteurs());
                    this.listeningTotalLengthLabel.setText(musiquesPlaylist.get(finalI+1).getDureeMusique().substring(3));
                }

                this.listeningProgressionSlider.setValue(Duration.ZERO.toSeconds());
                this.listeningCurrentTimeLabel.setText("00:00");
                playlistListeningMediaView.setMediaPlayer(this.mediaPlayer);
                this.mediaPlayer.play();
            });
        }
    }

    // "PLAY"/"PAUSE" ICON CLICKED
    public void playPauseButtonClicked(MouseEvent mouseEvent) {
        if (this.playPauseImageView.getImage().equals(this.pauseButtonImage)) {
            this.playPauseImageView.setImage(playButtonImage);
            mediaPlayer.pause();
        } else {
            this.playPauseImageView.setImage(pauseButtonImage);
            mediaPlayer.play();
        }
    }
}