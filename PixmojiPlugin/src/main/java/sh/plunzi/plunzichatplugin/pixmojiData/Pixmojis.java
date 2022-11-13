package sh.plunzi.plunzichatplugin.pixmojiData;

import org.bukkit.Bukkit;
import sh.plunzi.plunzichatplugin.chatSending.Debug;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pixmojis {
    public List<Pixmoji> pixmojiList = new ArrayList<>();

    public static Pixmoji nullmoji = new Pixmoji("null", '\uE000', "\0");

    public File pixmFile = new File(Bukkit.getServer().getWorldContainer(), "Pixmojis.pixm");
    final private char[] e = new char[]{'\uE001','\uE002','\uE003','\uE004','\uE005','\uE006','\uE007','\uE008','\uE009','\uE010','\uE011','\uE012','\uE013','\uE014','\uE015','\uE016','\uE017','\uE018','\uE019','\uE020','\uE021','\uE022','\uE023','\uE024','\uE025','\uE026','\uE027','\uE028','\uE029','\uE030','\uE031','\uE032','\uE033','\uE034','\uE035','\uE036','\uE037','\uE038','\uE039','\uE040','\uE041','\uE042','\uE043','\uE044','\uE045','\uE046','\uE047','\uE048','\uE049','\uE050','\uE051','\uE052','\uE053','\uE054','\uE055','\uE056','\uE057','\uE058','\uE059','\uE060','\uE061','\uE062','\uE063','\uE064'};
    final private char[] f = new char[]{'\uF001','\uF002','\uF003','\uF004','\uF005','\uF006','\uF007','\uF008','\uF009','\uF010','\uF011','\uF012','\uF013','\uF014','\uF015','\uF016','\uF017','\uF018','\uF019','\uF020','\uF021','\uF022','\uF023','\uF024','\uF025','\uF026','\uF027','\uF028','\uF029','\uF030','\uF031','\uF032','\uF033','\uF034','\uF035','\uF036','\uF037','\uF038','\uF039','\uF040','\uF041','\uF042','\uF043','\uF044','\uF045','\uF046','\uF047','\uF048','\uF049','\uF050','\uF051','\uF052','\uF053','\uF054','\uF055','\uF056','\uF057','\uF058','\uF059','\uF060','\uF061','\uF062','\uF063','\uF064'};


    public Pixmojis() {
        getPixmojis();
    }

    public void getPixmojis() {

        if(!pixmFile.exists()) {
            try {
                createFile();
            } catch (Exception e) {
                Debug.throwException(e);
                return;
            }
        }

        String line;

        try {
            FileReader fileReader = new FileReader(pixmFile, StandardCharsets.UTF_8);

            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                int lineNumber = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    lineNumber++;
                    try {

                        if(line.startsWith("\uD83D\uDCAC") || line.isBlank()) {
                            continue;
                        }

                        String name = line.split(":")[1];
                        String emoji = line.split(":")[0].substring(4);
                        char c;

                        if(line.startsWith("E")) {
                            c = e[Integer.parseInt(line.substring(1,4))-1];
                        } else if(line.startsWith("F")) {
                            c = f[Integer.parseInt(line.substring(1,4))-1];
                        } else {
                            throw new NumberFormatException("Line not starting with E, F or a comment");
                        }

                        pixmojiList.add(new Pixmoji(name, c, emoji));
                    } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException ignore) {
                        Debug.send("Error in " + pixmFile.getName() + "; line " + lineNumber);
                    }
                }
            }

        } catch (IOException e) {
            Debug.throwException(e);
        }

    }

    public void createFile() {
        try {
            if(pixmFile.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(pixmFile, true);
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        fileOutputStream, StandardCharsets.UTF_8));

                //Yeah this part of the code is horrible
                String content = "\uD83D\uDCACHere you can register your own emojis or change existing ones\n" + "\uD83D\uDCAC<id><emoji>:<name (no spaces)>:\n" + "\uD83D\uDCACid = <[E|F]xxx>\n" + "\uD83D\uDCAC\n" + "\uD83D\uDCACexample:\n" + "\uD83D\uDCACE051\uD83D\uDE33:flushed:\n" + "\uD83D\uDCACWhen you type :flushed: or \uD83D\uDE33 in chat, the icon corresponding to the ID will be displayed\n" + "\uD83D\uDCAC\n" + "\uD83D\uDCACIf you want multiple names to correspond to one pixmoji, register it two times, but set the emoji to null for the second time\n" + "\uD83D\uDCACexample:\n" + "\uD83D\uDCACF002\uD83E\uDDE1:orange_heart:\n" + "\uD83D\uDCACF002null:heart_orange:\n" + "\n" + "\uD83D\uDCACE = Smileys\n" + "E001\uD83D\uDC7F:angry_devil:\n" + "E002\uD83D\uDE04:happy:\n" + "E003\uD83E\uDD76:cold_face:\n" + "E004\uD83D\uDE18:kiss_heart:\n" + "E005\uD83D\uDE0B:tounge_out:\n" + "E006\uD83E\uDD2E:vomit:\n" + "E006null:pixmoji_giving_head_to_shrek:\n" + "E007\uD83E\uDD2D:hush:\n" + "E008\uD83E\uDEE2:hush2:\n" + "E009\uD83E\uDEE3:face_in_hands:\n" + "E010\uD83E\uDD28:raised_eyebrow:\n" + "E011\uD83D\uDE02:cry_laughing:\n" + "E012\uD83D\uDE1B:happy_tounge:\n" + "E013\uD83D\uDE00:happy2:\n" + "E014\uD83D\uDE05:awkward_smile:\n" + "E015\uD83D\uDE06:happy_squint:\n" + "E016\uD83E\uDD75:hot:\n" + "E017\uD83D\uDE1A:kiss_blush:\n" + "E018\uD83D\uDE19:kiss:\n" + "E019\uD83D\uDE2D:sob:\n" + "E020\uD83E\uDEE0:melting:\n" + "E021\uD83E\uDD11:money:\n" + "E022\uD83E\uDD22:nausea:\n" + "E023\uD83E\uDD7A:beg:\n" + "E024\uD83E\uDD23:rofl:\n" + "E025\uD83E\uDEE1:headpat:\n" + "E025\uD83E\uDEE1:salute:\n" + "E026\uD83E\uDD2B:shush:\n" + "E027☠:skull_bones:\n" + "E028\uD83D\uDC80:skull:\n" + "E029\uD83D\uDE34:sleeping:\n" + "E030\uD83D\uDE42:smile:\n" + "E031\uD83D\uDE07:halo:\n" + "E032\uD83D\uDE0D:heart_eyes:\n" + "E033\uD83E\uDD70:smile_hearts:\n" + "E034\uD83D\uDE08:evil_devil:\n" + "E035null:squint_eyes_hands:\n" + "E036☺:blush:\n" + "E037\uD83D\uDE0E:sunglasses:\n" + "E038\uD83D\uDE13:sweat:\n" + "E039\uD83E\uDD27:sneeze:\n" + "E040\uD83D\uDE1D:squint_tounge:\n" + "E041\uD83E\uDD29:starstruck:\n" + "E042\uD83E\uDD14:huh:\n" + "E042null:thinking:\n" + "E043\uD83D\uDE43:upside_down:\n" + "E044\uD83D\uDE09:wink:\n" + "E045null:wink2:\n" + "E046null:roll_wink:\n" + "E047\uD83E\uDD10:zipped:\n" + "E048null:uwu:\n" + "E049\uD83D\uDE10:neutral:\n" + "E050null:shocked:\n" + "E051\uD83D\uDE33:flushed:\n" + "E052null:wink3:\n" + "E053\uD83D\uDE36:no_mouth:\n" + "E054\uD83D\uDE36\u200D\uD83C\uDF2B️:face_in_clouds:\n" + "E055\uD83E\uDD25:lying:\n" + "E056null:dead:\n" + "E056null:dizzy:\n" + "E057\uD83D\uDE35:spiral_eyes:\n" + "E058\uD83D\uDE44:eyeroll:\n" + "E059\uD83E\uDD2F:exploding_head:\n" + "E060\uD83E\uDD20:cowboy:\n" + "E061\uD83E\uDD15:head_bandage:\n" + "E062\uD83D\uDE37:mask:\n" + "E063\uD83E\uDD13:nerd:\n" + "E064\uD83E\uDD73:party:\n" + "\n" + "\uD83D\uDCACF = Other Icons\n" + "F001❤:heart:\n" + "F001null:heart_red:\n" + "F001null:red_heart:\n" + "F002\uD83E\uDDE1:orange_heart:\n" + "F002null:heart_orange:\n" + "F003\uD83D\uDC9B:yellow_heart:\n" + "F003null:heart_yellow:\n" + "F003\uD83D\uDC9A:green_heart:\n" + "F004null:heart_green:\n" + "F004\uD83D\uDC99:blue_heart:\n" + "F004null:heart_blue:\n" + "F005\uD83D\uDC9C:purple_heart:\n" + "F005null:heart_purple:\n" + "F006\uD83E\uDD0E:brown_heart:\n" + "F006null:heart_brown:\n" + "F007\uD83D\uDDA4:black_heart:\n" + "F007null:heart_black:\n" + "F008\uD83E\uDD0D:white_heart:\n" + "F008null:heart_white:\n" + "F009\uD83D\uDC94:broken_heart:\n" + "F009null:heart_broken:\n" + "F010\uD83D\uDC40:eyes:\n" + "F010null:look:\n" + "F011\uD83C\uDF40:luck:\n" + "F011null:clover:";

                writer.write(content);

                writer.close();
                fileOutputStream.close();
            }

        } catch (IOException e) {
            Debug.throwException(e);
        }
    }

    public Pixmoji getPixmoji(String name) {

        for(Pixmoji pixmoji : pixmojiList) {
            if (name.equalsIgnoreCase(pixmoji.getName())) {
                return pixmoji;
            }
        }

        return nullmoji;
    }

    public Pixmoji getPixmoji(char unicodeChar) {

        for(Pixmoji pixmoji : pixmojiList) {
            if (unicodeChar == pixmoji.getUnicodeChar()) {
                return pixmoji;
            }
        }

        return nullmoji;
    }

    public Pixmoji translateEmoji(String emoji) {

        for(Pixmoji pixmoji : pixmojiList) {
            if (emoji.equals(pixmoji.getEmoji())) {
                return pixmoji;
            }
        }

        return nullmoji;
    }

    public Pixmoji getRandom() {
        Random random = new Random();
        int index = random.nextInt(pixmojiList.size());

        return pixmojiList.get(index);
    }

    public boolean isInList(char unicodeChar) {
        for(Pixmoji pixmoji : pixmojiList) {
            if (unicodeChar == pixmoji.getUnicodeChar()) {
                return true;
            }
        }
        return false;
    }

}

