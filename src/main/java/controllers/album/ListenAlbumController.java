package controllers.album;

import dao.AlbumDao;
import db.AlbumDb;
import db.MusiqueDb;
import dto.AlbumDto;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import listeners.album.ListAlbumSelectionListener;
import mapper.AlbumMapper;
import mapper.MusiqueMapper;
import utils.ListenUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListenAlbumController implements Initializable {

    // CURRENT MEDIA PLAYER
    private static MediaPlayer mediaPlayer;
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
    private MediaView albumListeningMediaView = new MediaView();

    @FXML
    private ImageView playPauseImageView = new ImageView();

    @FXML
    private Label listeningAlbumLabel = new Label();

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
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * ALBUM LISTENING COMPONENTS INITIALIZATION
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InvalidationListener listeningProgressionSliderListener;

        AlbumDto albumDto = ListAlbumSelectionListener.getAlbumSelected();
        AlbumDb albumDb = AlbumMapper.toDb(albumDto);
        AlbumDao albumDao = new AlbumDao();
        List<MusiqueDb> musiquesAlbum = albumDao.getMusiques(albumDb);
        List<MediaPlayer> albumTracks = new ArrayList<>();

        this.listeningAlbumLabel.setText(albumDb.getTitreAlbum());
        if (albumDb.getAnneeAlbum() != null) {
            this.listeningAlbumLabel.setText(this.listeningAlbumLabel.getText() + " (" + albumDb.getAnneeAlbum() + ")");
        }

        // MEDIA PLAYERS
        for (MusiqueDb musique : musiquesAlbum) {
            albumTracks.add(new MediaPlayer(new Media(new File(musique.getNomFichierMusique()).toURI().toString())));
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
        mediaPlayer = albumTracks.get(0);
        mediaPlayer.setOnReady(() -> totalLength = mediaPlayer.getMedia().getDuration());
        mediaPlayer.currentTimeProperty().addListener(mediaPlayerListener);
        this.albumListeningMediaView.setMediaPlayer(mediaPlayer);
        this.listeningMusicTitleLabel.setText(musiquesAlbum.get(0).getTitreMusique());
        this.listeningProgressionSlider.setValue(Duration.ZERO.toSeconds());
        this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesAlbum.get(0)).getAuteurs());
        this.listeningCurrentTimeLabel.setText("00:00");
        this.listeningTotalLengthLabel.setText(musiquesAlbum.get(0).getDureeMusique().substring(3));

        // MUSICS PLAYING LINKING PREPARATION
        for (int i = 0; i < albumTracks.size(); i++) {
            MediaPlayer finalMediaPlayer = albumTracks.get(i);
            MediaPlayer nextMediaPlayer;
            if (i == albumTracks.size() - 1) {
                nextMediaPlayer = albumTracks.get(0);
            } else {
                nextMediaPlayer = albumTracks.get(i + 1);
            }

            MediaPlayer finalNextMediaPlayer = nextMediaPlayer;
            int finalI = i;

            finalMediaPlayer.setOnEndOfMedia(() -> {
                finalMediaPlayer.currentTimeProperty().removeListener(mediaPlayerListener);
                mediaPlayer = finalNextMediaPlayer;
                totalLength = mediaPlayer.getMedia().getDuration();
                mediaPlayer.currentTimeProperty().addListener(mediaPlayerListener);
                this.playPauseImageView.setImage(this.pauseButtonImage);

                if (finalI == albumTracks.size() - 1) {
                    this.listeningMusicTitleLabel.setText(musiquesAlbum.get(0).getTitreMusique());
                    this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesAlbum.get(0)).getAuteurs());
                    this.listeningTotalLengthLabel.setText(musiquesAlbum.get(0).getDureeMusique().substring(3));
                } else {
                    this.listeningMusicTitleLabel.setText(musiquesAlbum.get(finalI + 1).getTitreMusique());
                    this.listeningMusicArtistsLabel.setText(MusiqueMapper.toDto(musiquesAlbum.get(finalI + 1)).getAuteurs());
                    this.listeningTotalLengthLabel.setText(musiquesAlbum.get(finalI + 1).getDureeMusique().substring(3));
                }

                this.listeningProgressionSlider.setValue(Duration.ZERO.toSeconds());
                this.listeningCurrentTimeLabel.setText("00:00");
                albumListeningMediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
            });
        }
    }

    // "PLAY"/"PAUSE" ICON CLICKED
    public void playPauseButtonClicked() {
        if (this.playPauseImageView.getImage().equals(this.pauseButtonImage)) {
            this.playPauseImageView.setImage(playButtonImage);
            mediaPlayer.pause();
        } else {
            this.playPauseImageView.setImage(pauseButtonImage);
            mediaPlayer.play();
        }
    }
}