package com.example.sepgorfront01;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javafx.stage.Popup;
import javafx.stage.Stage;
import pkg.*;
import pkg.Menu;

public class HelloController {
    @FXML
    public BorderPane chatScene;
    @FXML
    private VBox chatsVBox;
    @FXML
    private Label welcomeText;
    @FXML
    private TextArea emailSignUp;
    @FXML
    private Text signInSignUpText;
    @FXML
    private Button signInSignUpButton;
    @FXML
    private Text signInSignUpShowText;
    @FXML
    private TextArea userNameInput;
    @FXML
    private TextArea passwordInput;
    @FXML
    public VBox signUpSignInVBox;
    @FXML
    private ScrollPane chatsVBoxMessages;
    @FXML
    private VBox chatSettings1;
    @FXML
    private TextField newChatName;
    @FXML
    private TextField newUserNameToAdd;
    @FXML
    private TextField newChannelName;
    @FXML
    private CheckBox canAddChannel;
    @FXML
    private CheckBox canRemoveChannel;
    @FXML
    private CheckBox canRemoveUser;
    @FXML
    private CheckBox canRestrictUser;
    @FXML
    private CheckBox canBanUser;
    @FXML
    private CheckBox canChangeServerName;
    @FXML
    private CheckBox canPin;
    @FXML
    private TextField newRoleUserName;
    @FXML
    private TextField userNameToRemove;
    @FXML
    private VBox channelsVBox;
    @FXML
    private Button chatSettingsButton;
    @FXML
    private Button pinsButton;
    @FXML
    private VBox usersOfServer;
    @FXML
    private VBox userSettingPane;
    @FXML
    private Text serverName;
    @FXML
    private Text usersUsername;
    @FXML
    private VBox chatMessages;
    @FXML
    private VBox chatsMessenger;
    @FXML
    private Label selectedMessageId;

    @FXML
    private TextField changeUsername;
    @FXML
    private TextField changeEmail;
    @FXML
    private TextField changePhoneNumber;
    @FXML
    private TextField changePassword;
    @FXML
    private TextField newServerName;

    @FXML
    private Pane addFriendPane;
    @FXML
    private Pane onlinePane;
    @FXML
    private Pane allPane;
    @FXML
    private Pane pendingPane;
    @FXML
    private Pane blockedPane;

    @FXML
    private TextArea newUsernameToAddToFriends;
    @FXML
    private VBox pendingVBox;
    @FXML
    private VBox allVBox;
    @FXML
    private VBox onlineVBox;
    @FXML
    private VBox blockedVBox;
    @FXML
    public ChoiceBox changeStatusChoiceBox;
    @FXML
    private Label unsuccesfulLabel;

    @FXML
    private TextField filePathInput;
    @FXML
    private Button sendFileButton;


    @FXML
    private TextField textMessage;
    @FXML
    private Label replyMesageId;
    @FXML
    private TextField profLink;
    @FXML
    private TextField serverProfLink;
    @FXML
    private ImageView imageLink;
    @FXML
    private VBox VBoxInfo;

    @FXML
    private Button voiceChat;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    private boolean onSignIN = true;

    public void sayHi() {

    }

    public static MouseEvent event;

    @FXML
    protected void signInSignUpSendButton(MouseEvent event) throws NoSuchAlgorithmException, LineUnavailableException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException, InvalidKeyException, InterruptedException {
        String userName = userNameInput.getText();
        String password = passwordInput.getText();
        Main.content.session = "";
        Main.content.privateKey = "";
        Main.content.publicKey = "";
        unsuccesfulLabel.setVisible(false);
        if (onSignIN) {
            this.event = event;
            System.out.println("login with: username: " + userName + "   password: " + password);
            //System.out.println(new Gson().toJson(new Command("sa",1,1,new Message(),1)));
            Main.content.validSession(Main.client.socket, Main.client.inputClient, userName, password);
            // pkg.Menu.signIn(userName,password);
            //chatsVBox.getChildren().add(new Text("hiiii"));


        } else {
            String email = emailSignUp.getText();
            System.out.println("signUp with: username: " + userName + "   password: " + password + "   email: " + email);

            ///regex
            pkg.Menu.signUp(userName, password, email);
        }

    }

    @FXML
    protected void signUp() {
        unsuccesfulLabel.setVisible(false);
        if (onSignIN) {
            emailSignUp.setVisible(true);
            emailSignUp.setManaged(true);
            signInSignUpText.setText("signUp");
            signInSignUpButton.setText("sign up");
            signInSignUpShowText.setText("already have an account?");
            onSignIN = false;
        } else {
            emailSignUp.setVisible(false);
            emailSignUp.setManaged(false);
            signInSignUpText.setText("signIn");
            signInSignUpButton.setText("sign in");
            signInSignUpShowText.setText("doesn't have an account?");
            onSignIN = true;
        }
    }

    @FXML
    public void setUsername(String usernagme) {
        System.out.println(usernagme);
        usersUsername.setText(usernagme);
    }

    @FXML
    public void addChatToChats(String id, String name) {
        Text txt = new Text(name);
        txt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    try {
                        imageLink.setImage(new Image("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\serverProf\\" + (id) + ".jpeg"));
                    } catch (Exception e) {
                        imageLink.setImage(new Image("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\serverProf\\unnamed.png"));
                    }

                    onUser = false;
                    onChat = true;
                    Main.showChat(Integer.parseInt(id));
                    serverName.setText(name);
                    chatSettingsButton.setDisable(false);

                    voiceChat.setDisable(false);
                    pinsButton.setDisable(false);
                    userSettingPane.setVisible(false);
                    chatsVBoxMessages.setVisible(true);
                    chatsMessenger.setVisible(true);
                    Main.showChannels(Integer.parseInt(id));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatsVBox.getChildren().add(txt);


            }
        });
    }

    @FXML
    public void sendTextMessage() {
        if (Integer.parseInt(replyMesageId.getText()) > 0) {
            Main.sendMessage(Main.chatId, textMessage.getText(), Main.chatEncryption, Integer.parseInt(replyMesageId.getText()));
        } else {
            Main.sendMessage(Main.chatId, textMessage.getText(), Main.chatEncryption);
        }
        textMessage.clear();
    }

    @FXML
    public void clearChat() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatMessages.getChildren().removeAll(chatMessages.getChildren());
            }
        });
    }

    @FXML
    public void clearChannels() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                channelsVBox.getChildren().removeAll(channelsVBox.getChildren());
            }
        });
    }

    @FXML
    public void clearUsersOfServer() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usersOfServer.getChildren().removeAll(usersOfServer.getChildren());
            }
        });
    }

    @FXML
    public void clearPendingVBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pendingVBox.getChildren().removeAll(pendingVBox.getChildren());
            }
        });
    }

    @FXML
    public void clearAllVBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                allVBox.getChildren().removeAll(allVBox.getChildren());
            }
        });
    }

    @FXML
    public void clearOnlineVBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                onlineVBox.getChildren().removeAll(onlineVBox.getChildren());
            }
        });
    }

    @FXML
    public void clearBlockedVBox() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blockedVBox.getChildren().removeAll(blockedVBox.getChildren());
            }
        });
    }

    @FXML
    protected void addLike() {
        Main.addReact(Main.chatId, Integer.parseInt(selectedMessageId.getText()), 1);
    }

    @FXML
    protected void addDisLike() {
        Main.addReact(Main.chatId, Integer.parseInt(selectedMessageId.getText()), 2);
    }

    @FXML
    protected void addLaugh() {
        Main.addReact(Main.chatId, Integer.parseInt(selectedMessageId.getText()), 3);
    }

    boolean onChat = false;
    boolean onUser = true;
    boolean onFriend = false;

    @FXML
    protected void chatSettingsButton() {
        if (!onUser) {
            if (onChat) {
                chatsVBoxMessages.setVisible(false);
                chatsMessenger.setVisible(false);
                chatSettings1.setVisible(true);
                onChat = false;
                Main.getUsersOfChat(Main.chatId);

            } else {
                chatsVBoxMessages.setVisible(true);
                chatsMessenger.setVisible(true);
                chatSettings1.setVisible(false);
                onChat = true;
            }
        } else {
            if (onFriend) {
                chatsVBoxMessages.setVisible(false);
                chatsMessenger.setVisible(false);
                userSettingPane.setVisible(true);
                pinsButton.setDisable(true);
                voiceChat.setDisable(true);
                onFriend = false;
            } else {

                chatsVBoxMessages.setVisible(true);
                chatsMessenger.setVisible(true);
                userSettingPane.setVisible(false);
                onFriend = true;

                pinsButton.setDisable(false);
                voiceChat.setDisable(false);
            }

        }


    }

    @FXML
    protected void newChatNameButton() {
        Main.changeServerName(Main.chatId, newChatName.getText());
        newChatName.clear();
    }

    @FXML
    protected void newUserNameToAddButton() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Main.addToChat(Main.chatId, newUserNameToAdd.getText(), Main.chatEncryption);
        newUserNameToAdd.clear();
    }

    @FXML
    protected void newChannelNameButton() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Main.addChannel(Main.chatId, newChannelName.getText());
        newChannelName.clear();
    }

    @FXML
    protected void addNewRoleButton() {
        StringBuilder roles = new StringBuilder("00000010");

        if (canAddChannel.isSelected()) roles.setCharAt(0, '1');
        if (canRemoveChannel.isSelected()) roles.setCharAt(1, '1');
        if (canRemoveUser.isSelected()) roles.setCharAt(2, '1');
        if (canRestrictUser.isSelected()) roles.setCharAt(3, '1');
        if (canBanUser.isSelected()) roles.setCharAt(4, '1');
        if (canChangeServerName.isSelected()) roles.setCharAt(5, '1');
        if (canPin.isSelected()) roles.setCharAt(7, '1');
        Main.addRole(newRoleUserName.getText(), Main.chatId, String.valueOf(roles));
        newRoleUserName.clear();
        canAddChannel.setSelected(false);
        canRemoveChannel.setSelected(false);
        canRemoveUser.setSelected(false);
        canRestrictUser.setSelected(false);
        canBanUser.setSelected(false);
        canChangeServerName.setSelected(false);
        canPin.setSelected(false);


    }

    boolean isOnChannel = false;

    @FXML
    public void addChannelsToChannelsVBox(String id, String name) {

        Text txt = new Text(name);
        txt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Main.showChat(Integer.parseInt(id));
                    chatSettingsButton.setDisable(true);
                    isOnChannel = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                channelsVBox.getChildren().add(txt);


            }
        });
    }

    @FXML
    public void addFriendsToChannelsVBox(String id, String name, String status) {

        Text txt = new Text(name + " : " + status);
        txt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    try {
                        imageLink.setImage(new Image("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\userProf\\" + (id) + ".jpeg"));
                    } catch (Exception e) {
                        imageLink.setImage(new Image("C:\\Users\\NP\\IdeaProjects\\sepgor05\\src\\serverProf\\unnamed.png"));
                    }

                    Main.showChat(Integer.parseInt(id));
                    chatSettingsButton.setDisable(false);
                    isOnChannel = false;
                    onFriend = true;
                    pinsButton.setDisable(false);
                    voiceChat.setDisable(false);
                    chatsMessenger.setVisible(true);
                    chatsVBoxMessages.setVisible(true);
                    userSettingPane.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                channelsVBox.getChildren().add(txt);


            }
        });
    }

    @FXML
    public void addUsersToChatUSers(String name) {
        Text txt = new Text(name);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                usersOfServer.getChildren().add(txt);
            }
        });
    }

    @FXML
    protected void userNameToRemoveButton() {
        Main.removeFromChat(Main.chatId, userNameToRemove.getText());
        userNameToRemove.clear();

    }

    @FXML
    public void addMessageToChat(int id, String user, String message, String dateTS, String reacts) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().add(new Label(user));
        VBox vbox = new VBox();
        hBox.getChildren().add(vbox);
        vbox.getChildren().add(new Text(message));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dateTS));
        vbox.getChildren().add(new Text(sf.format(date)));
        vbox.getChildren().add(new Label(reacts));
        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedMessageId.setText(String.valueOf(id));
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatMessages.getChildren().add(hBox);
            }
        });
    }

    @FXML
    public void addMessageToChatT2(int id, String user, String message, String dateTS, String reply, String reacts) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().add(new Label(user));
        VBox vbox = new VBox();
        hBox.getChildren().add(vbox);
        vbox.getChildren().add(new Text("is reply to: " + reply));
        vbox.getChildren().add(new Text(message));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dateTS));
        vbox.getChildren().add(new Text(sf.format(date)));
        vbox.getChildren().add(new Label(reacts));
        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectedMessageId.setText(String.valueOf(id));
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatMessages.getChildren().add(hBox);
            }
        });
    }

    @FXML
    public void addMessageToChatT3(int id, String user, String message, String dateTS, String reacts) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().add(new Label(user));
        VBox vbox = new VBox();
        hBox.getChildren().add(vbox);
        Text newFileText = new Text(message + "  download");
        newFileText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //selectedMessageId.setText(String.valueOf(id));
                Main.getFile(message);
            }
        });
        vbox.getChildren().add(newFileText);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.parseLong(dateTS));
        vbox.getChildren().add(new Text(sf.format(date)));
        vbox.getChildren().add(new Label(reacts));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatMessages.getChildren().add(hBox);
            }
        });
    }


    @FXML
    protected void replyButtonClick() {
        if (Integer.parseInt(replyMesageId.getText()) > 0) {
            replyMesageId.setText("0");
            selectedMessageId.setText("0");
        } else {
            replyMesageId.setText(selectedMessageId.getText());
        }
    }

    @FXML
    public void userSettingPaneButton() {
        Main.getFriends();
        onChat = false;
        onUser = true;
        serverName.setText(usersUsername.getText());
        chatSettingsButton.setDisable(false);
        pinsButton.setDisable(true);
        voiceChat.setDisable(true);
        userSettingPane.setVisible(true);
        chatsVBoxMessages.setVisible(false);
        chatsMessenger.setVisible(false);
    }

    @FXML
    protected void changeUsernameButton() {
        Main.changeUsername(changeUsername.getText());
        changeUsername.clear();
    }

    @FXML
    protected void changeEmailButton() {
        Main.changeEmail(changeEmail.getText());
        changeEmail.clear();
    }

    @FXML
    protected void changePhoneNumberButton() {
        Main.changePhoneNumber(changePhoneNumber.getText());
        changePhoneNumber.clear();
    }

    @FXML
    protected void changePasswordButton() {
        Main.changePassword(changePhoneNumber.getText());
        changePhoneNumber.clear();
    }

    @FXML
    protected void newServerNameButton() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Main.createChat(newServerName.getText());
        newServerName.clear();
    }

    @FXML
    protected void showOnlines() {
        Main.getFriends();
        addFriendPane.setVisible(false);
        onlinePane.setVisible(true);
        allPane.setVisible(false);
        pendingPane.setVisible(false);
        blockedPane.setVisible(false);
    }

    @FXML
    protected void showAll() {
        Main.getFriends();
        addFriendPane.setVisible(false);
        onlinePane.setVisible(false);
        allPane.setVisible(true);
        pendingPane.setVisible(false);
        blockedPane.setVisible(false);
    }

    @FXML
    protected void showPending() {
        Main.getSentPending();
        Main.getPending();
        addFriendPane.setVisible(false);
        onlinePane.setVisible(false);
        allPane.setVisible(false);
        pendingPane.setVisible(true);
        blockedPane.setVisible(false);
    }

    @FXML
    protected void showBlocked() {
        Main.getBlocks();
        addFriendPane.setVisible(false);
        onlinePane.setVisible(false);
        allPane.setVisible(false);
        pendingPane.setVisible(false);
        blockedPane.setVisible(true);
    }

    @FXML
    protected void showAddFriend() {
        addFriendPane.setVisible(true);
        onlinePane.setVisible(false);
        allPane.setVisible(false);
        pendingPane.setVisible(false);
        blockedPane.setVisible(false);
    }

    @FXML
    protected void pinsButton() {
        Main.showPins(Main.chatId);
    }

    Socket voiceChatclient=null;

    @FXML
    protected void startVoiceChat() throws IOException, LineUnavailableException {
        System.out.println("start voiceChat");
        if (voiceChatclient!=null) {

            voiceChatclient.close();
            voiceChatclient=null;
        } else {
            voiceChatclient = new Socket("localhost", 10002);
            Thread inputThread = new Thread(new InputThread(voiceChatclient));
            Thread outputThread = new Thread(new OutputThread(voiceChatclient));
            inputThread.start();
            outputThread.start();
        }
    }

    @FXML
    protected void pinButton() {
        if (Integer.parseInt(selectedMessageId.getText()) > 0) {
            Main.addPin(Main.chatId, Integer.parseInt(selectedMessageId.getText()));
            selectedMessageId.setText("0");
        }
    }

    @FXML
    protected void newUsernameToAddToFriendsButton() throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        System.out.println("add friend " + newUsernameToAddToFriends.getText());
        Main.createPv(newUsernameToAddToFriends.getText());
        newUsernameToAddToFriends.clear();
    }

    @FXML
    public void addFriendsToPending(String id, String name) {
        Text txt = new Text(name);
        Text txtY = new Text("accept");
        Text txtN = new Text("decline");
        txtY.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.addFriend(Integer.parseInt(id));
                Main.getPending();
                Main.getSentPending();
            }
        });
        txtN.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.rejectFriend(Integer.parseInt(id));
                Main.getPending();
                Main.getSentPending();
            }
        });
        HBox nhbox = new HBox();
        nhbox.getChildren().add(txt);
        nhbox.getChildren().add(txtY);
        nhbox.getChildren().add(txtN);
        nhbox.setSpacing(10);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pendingVBox.getChildren().add(nhbox);
            }
        });
    }

    @FXML
    public void addFriendsToPending2(String id, String name) {
        Text txt = new Text(name);
        Text txtY = new Text("unsend");
        txtY.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.unSendPending(Integer.parseInt(id));
                Main.getPending();
                Main.getSentPending();
            }
        });

        HBox nhbox = new HBox();
        nhbox.getChildren().add(txt);
        nhbox.getChildren().add(txtY);
        nhbox.setSpacing(10);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pendingVBox.getChildren().add(nhbox);
            }
        });
    }

    @FXML
    public void addFriendsToAll(String id, String name) {
        Text txt = new Text(name);
        Text txtY = new Text("block!");
        txtY.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.blockChat(Integer.parseInt(id));
                Main.getFriends();
            }
        });

        HBox nhbox = new HBox();
        nhbox.getChildren().add(txt);
        nhbox.getChildren().add(txtY);
        nhbox.setSpacing(10);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                allVBox.getChildren().add(nhbox);
            }
        });
    }

    @FXML
    public void addFriendsToOnlineVBox(String id, String name) {
        Text txt = new Text(name);


        HBox nhbox = new HBox();
        nhbox.getChildren().add(txt);
        nhbox.setSpacing(10);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                onlineVBox.getChildren().add(nhbox);
            }
        });
    }

    @FXML
    public void addBlockedToBlockedVBox(String id, String name) {
        Text txt = new Text(name);
        Text txtY = new Text("unblock!");
        txtY.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.unBlock(Integer.parseInt(id));
                Main.getBlocks();
            }
        });

        HBox nhbox = new HBox();
        nhbox.getChildren().add(txt);
        nhbox.getChildren().add(txtY);
        nhbox.setSpacing(10);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                blockedVBox.getChildren().add(nhbox);
            }
        });
    }

    @FXML
    protected void changeStatusChoiceBox() {
        //System.out.println(((String) changeStatusChoiceBox.getValue()));
        Main.changeStats(Integer.parseInt((String) changeStatusChoiceBox.getValue()));
    }

    @FXML
    public void alertUnsuccessful() {

        unsuccesfulLabel.setVisible(true);
    }

    @FXML
    public void showHome(String username) throws IOException {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                loader.setController(Main.controller);
                try {
                    stage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.show();

                chatScene.setVisible(true);
                chatScene.setManaged(true);
                signUpSignInVBox.setVisible(false);
                signUpSignInVBox.setManaged(false);

                pkg.Main.getServers();
                userSettingPaneButton();
                /////first to start modify
                changeStatusChoiceBox.getItems().add("0");
                changeStatusChoiceBox.getItems().add("1");
                changeStatusChoiceBox.getItems().add("2");
                changeStatusChoiceBox.getItems().add("3");
                changeStatusChoiceBox.getItems().add("4");
                setUsername(username);
            }
        });

    }

    @FXML
    protected void fileButton() {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("file.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Popup popup = new Popup();
        popup.getContent().setAll(root);
        popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    boolean onSendFile = false;

    @FXML
    protected void showFile() {
        if (!onSendFile) {
            sendFileButton.setVisible(true);
            filePathInput.setVisible(true);
            onSendFile = true;
        } else {
            sendFileButton.setVisible(false);
            filePathInput.setVisible(false);
            onSendFile = false;
        }
    }

    @FXML
    protected void sendFile() {
        System.out.println(filePathInput.getText());
        new Thread(new ClientFile(filePathInput.getText(), HelloApplication.fileSocket, 1)).start();
        filePathInput.clear();
    }

    @FXML
    protected void profLinkSend() {
        new Thread(new ClientFile(profLink.getText(), HelloApplication.fileSocket, 2)).start();
        profLink.clear();
    }

    @FXML
    protected void sendServerProf() {
        new Thread(new ClientFile(serverProfLink.getText(), HelloApplication.fileSocket, 3)).start();
        serverProfLink.clear();
    }


}

