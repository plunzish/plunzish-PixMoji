package sh.plunzi.plunzichatplugin.pixmojiData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pixmojis {
    public List<Pixmoji> pixmojiList = new ArrayList<>();

    public static Pixmoji nullmoji = new Pixmoji("null", '\uE000', "\0");

    public Pixmojis() {
        pixmojiList.add(new Pixmoji("angryDevil", '\uE001', "\uD83D\uDC7F"));
        pixmojiList.add(new Pixmoji("happy", '\uE002', "\uD83D\uDE04"));
        pixmojiList.add(new Pixmoji("coldFace", '\uE003', "\uD83E\uDD76"));
        pixmojiList.add(new Pixmoji("kissHeart", '\uE004', "\uD83D\uDE18"));
        pixmojiList.add(new Pixmoji("toungeOut", '\uE005', "\uD83D\uDE0B"));
        pixmojiList.add(new Pixmoji("vomit", '\uE006', "\uD83E\uDD2E"));
        pixmojiList.add(new Pixmoji("pixmojiGivingHeadToShrek", '\uE006', "\uD83E\uDD2E"));
        pixmojiList.add(new Pixmoji("hush", '\uE007', "\uD83E\uDD2D"));
        pixmojiList.add(new Pixmoji("hush2", '\uE008', "\uD83E\uDEE2"));
        pixmojiList.add(new Pixmoji("faceInHands", '\uE009', "\uD83E\uDEE3"));
        pixmojiList.add(new Pixmoji("raisedEyeBrow", '\uE010', "\uD83E\uDD28"));
        pixmojiList.add(new Pixmoji("cryLaugh", '\uE011', "\uD83D\uDE02"));
        pixmojiList.add(new Pixmoji("happyTounge", '\uE012', "\uD83D\uDE1B"));
        pixmojiList.add(new Pixmoji("happy2", '\uE013', "\uD83D\uDE00"));
        pixmojiList.add(new Pixmoji("awkwardSmile", '\uE014', "\uD83D\uDE05"));
        pixmojiList.add(new Pixmoji("happySquint", '\uE015', "\uD83D\uDE06"));
        pixmojiList.add(new Pixmoji("hot", '\uE016', "\uD83E\uDD75"));
        pixmojiList.add(new Pixmoji("kissBlush", '\uE017', "\uD83D\uDE1A"));
        pixmojiList.add(new Pixmoji("kiss", '\uE018', "\uD83D\uDE19"));
        pixmojiList.add(new Pixmoji("sob", '\uE019', "\uD83D\uDE2D"));
        pixmojiList.add(new Pixmoji("melting", '\uE020', "\uD83E\uDEE0"));
        pixmojiList.add(new Pixmoji("money", '\uE021', "\uD83E\uDD11"));
        pixmojiList.add(new Pixmoji("nausea", '\uE022', "\uD83E\uDD22"));
        pixmojiList.add(new Pixmoji("beg", '\uE023', "\uD83E\uDD7A"));
        pixmojiList.add(new Pixmoji("rofl", '\uE024', "\uD83E\uDD23"));
        pixmojiList.add(new Pixmoji("headpat", '\uE025', "\uD83E\uDEE1"));
        pixmojiList.add(new Pixmoji("salute", '\uE025', "\uD83E\uDEE1"));
        pixmojiList.add(new Pixmoji("shush", '\uE026', "\uD83E\uDD2B"));
        pixmojiList.add(new Pixmoji("skullBones", '\uE027', "\u2620"));
        pixmojiList.add(new Pixmoji("skull", '\uE028', "\uD83D\uDC80"));
        pixmojiList.add(new Pixmoji("sleeping", '\uE029', "\uD83D\uDE34"));
        pixmojiList.add(new Pixmoji("smile", '\uE030', "\uD83D\uDE42"));
        pixmojiList.add(new Pixmoji("halo", '\uE031', "\uD83D\uDE07"));
        pixmojiList.add(new Pixmoji("heartEyes", '\uE032', "\uD83D\uDE0D"));
        pixmojiList.add(new Pixmoji("smileHearts", '\uE033', "\uD83E\uDD70"));
        pixmojiList.add(new Pixmoji("evilDevil", '\uE034', "\uD83D\uDE08"));
        pixmojiList.add(new Pixmoji("squintEyesHands", '\uE035', "\0"));
        pixmojiList.add(new Pixmoji("blush", '\uE036', "\u263A"));
        pixmojiList.add(new Pixmoji("sunglasses", '\uE037', "\uD83D\uDE0E"));
        pixmojiList.add(new Pixmoji("sweat", '\uE038', "\uD83D\uDE13"));
        pixmojiList.add(new Pixmoji("sneeze", '\uE039', "\uD83E\uDD27"));
        pixmojiList.add(new Pixmoji("squintTounge", '\uE040', "\uD83D\uDE1D"));
        pixmojiList.add(new Pixmoji("starstruck", '\uE041', "\uD83E\uDD29"));
        pixmojiList.add(new Pixmoji("huh", '\uE042', "\uD83E\uDD14"));
        pixmojiList.add(new Pixmoji("thinking", '\uE043', "\uD83E\uDD14"));
        pixmojiList.add(new Pixmoji("upsideDown", '\uE042', "\uD83D\uDE43"));
        pixmojiList.add(new Pixmoji("wink", '\uE043', "\uD83D\uDE09"));
        pixmojiList.add(new Pixmoji("wink2", '\uE044', "\0"));
        pixmojiList.add(new Pixmoji("rollWink", '\uE045', "\0"));
        pixmojiList.add(new Pixmoji("zipped", '\uE047', "\uD83E\uDD10"));
        pixmojiList.add(new Pixmoji("uwu", '\uE048', "\0"));
        pixmojiList.add(new Pixmoji("neutral", '\uE049', "\uD83D\uDE10"));
        pixmojiList.add(new Pixmoji("shocked", '\uE050', "\0"));
        pixmojiList.add(new Pixmoji("flushed", '\uE051', "\uD83D\uDE33"));
        pixmojiList.add(new Pixmoji("wink3", '\uE052', "\0"));
        pixmojiList.add(new Pixmoji("noMouth", '\uE053', "\uD83D\uDE36"));
        pixmojiList.add(new Pixmoji("faceInClouds", '\uE054', "\uD83D\uDE36\u200D\uD83C\uDF2B\uFE0F"));
        pixmojiList.add(new Pixmoji("lying", '\uE055', "\uD83E\uDD25"));
        pixmojiList.add(new Pixmoji("dead", '\uE056', "\0"));
        pixmojiList.add(new Pixmoji("dizzy", '\uE056', "\0"));
        pixmojiList.add(new Pixmoji("spiralEyes", '\uE057', "\uD83D\uDE35"));
        pixmojiList.add(new Pixmoji("eyeroll", '\uE058', "\uD83D\uDE44"));
        pixmojiList.add(new Pixmoji("explodingHead", '\uE059', "\uD83E\uDD2F"));
        pixmojiList.add(new Pixmoji("cowboy", '\uE060', "\uD83E\uDD20"));
        pixmojiList.add(new Pixmoji("headBandage", '\uE061', "\uD83E\uDD15"));
        pixmojiList.add(new Pixmoji("mask", '\uE062', "\uD83D\uDE37"));
        pixmojiList.add(new Pixmoji("nerd", '\uE063', "\uD83E\uDD13"));
        pixmojiList.add(new Pixmoji("party", '\uE064', "\uD83E\uDD73"));



        pixmojiList.add(new Pixmoji("heart", '\uF001', "\u2764"));
        pixmojiList.add(new Pixmoji("heart_red", '\uF001', "\u2764"));
        pixmojiList.add(new Pixmoji("orange_heart", '\uF002', "\uD83E\uDDE1"));
        pixmojiList.add(new Pixmoji("heart_orange", '\uF002', "\uD83E\uDDE1"));
        pixmojiList.add(new Pixmoji("yellow_heart", '\uF003', "\uD83D\uDC9B"));
        pixmojiList.add(new Pixmoji("heart_yellow", '\uF003', "\uD83D\uDC9B"));
        pixmojiList.add(new Pixmoji("green_heart", '\uF004', "\uD83D\uDC9A"));
        pixmojiList.add(new Pixmoji("heart_green", '\uF004', "\uD83D\uDC9A"));
        pixmojiList.add(new Pixmoji("blue_heart", '\uF005', "\uD83D\uDC99"));
        pixmojiList.add(new Pixmoji("heart_blue", '\uF005', "\uD83D\uDC99"));
        pixmojiList.add(new Pixmoji("purple_heart", '\uF006', "\uD83D\uDC9C"));
        pixmojiList.add(new Pixmoji("heart_purple", '\uF006', "\uD83D\uDC9C"));
        pixmojiList.add(new Pixmoji("brown_heart", '\uF007', "\uD83E\uDD0E"));
        pixmojiList.add(new Pixmoji("heart_brown", '\uF007', "\uD83E\uDD0E"));
        pixmojiList.add(new Pixmoji("black_heart", '\uF008', "\uD83D\uDDA4"));
        pixmojiList.add(new Pixmoji("heart_black", '\uF008', "\uD83D\uDDA4"));
        pixmojiList.add(new Pixmoji("white_heart", '\uF009', "\uD83E\uDD0D"));
        pixmojiList.add(new Pixmoji("heart_white", '\uF009', "\uD83E\uDD0D"));
        pixmojiList.add(new Pixmoji("broken_heart", '\uF010', "\uD83E\uDD0D"));
        pixmojiList.add(new Pixmoji("heart_broken", '\uF010', "\uD83E\uDD0D"));
        pixmojiList.add(new Pixmoji("eyes", '\uF010', "\uD83D\uDC40"));
        pixmojiList.add(new Pixmoji("look", '\uF010', "\uD83D\uDC40"));
        pixmojiList.add(new Pixmoji("clover", '\uF011', "\uD83C\uDF40"));
        pixmojiList.add(new Pixmoji("luck", '\uF011', "\uD83C\uDF40"));

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

    public boolean isInList(String name) {
        for(Pixmoji pixmoji : pixmojiList) {
            if (name.equals(pixmoji.getName())) {
                return true;
            }
        }
        return false;
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

