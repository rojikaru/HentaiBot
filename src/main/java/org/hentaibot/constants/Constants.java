package org.hentaibot.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final List<String> TAGS = Arrays.asList(
            "ass", "anal", "cum", "creampie", "hentai", "masturbation",
            "public", "orgy", "elf", "yuri", "pussy", "glasses", "blowjob",
            "handjob", "footjob", "boobs", "thighs", "ahe_gao", "uniform", "gangbang", "tentacles", "genshin_impact", "naruto", "bleach"
    );

    public static final List<String> ARTISTS = Arrays.asList(
            "shexyo", "theobrobine", "cutesexyrobutts", "sakimichan", "krabby_(artist)", "lexaiduer", "miraihikari",
            "LumiNyu", "lime_(purple_haze", "nepcill", "sciamano240", "neoartcore", "dandon_fuga", "zaphn", "flou",
            "prywinko", "alexander_dinh", "kittew", "supullim", "tofuubear", "azto_dio", "derpixon", "kinkymation", "ggc", "afrobull"
    );

    public static final List<String> SFW_CATEGORIES = Arrays.asList(
            "waifu", "neko", "shinobu", "megumin", "hug", "awoo", "kiss", "lick", "smug", "blush", "smile", "wave", "nom", "glomp", "slap", "kill", "happy", "wink", "poke", "dance", "cringe"
    );

    public static final List<String> PICTURE_EXTENSIONS =
            Arrays.asList(".jpg", ".jpeg", ".png", ".webp", ".avif", ".jfif");

    public static final Map<String, String> MESSAGES = Map.of(
            "UNRECOGNIZED_INPUT", "I don't understand you. For list of all supported commands, try /help",
            "START", """
                    Hi! I'm HentaiBot. I can send you some spicy anime pics.
                    Use me if you're 18+.
                    For list of all supported commands, try /help
                    """,
            "HELP", """
                    List of commands available for now:
                    /help
                    /hentai
                    /anime
                    /help_tags
                    """,
            "HELP_TAGS", """
                    List of tags available for now:
                    /tags_anal
                    /tags_cum
                    /tags_creampie
                    /tags_hentai
                    /tags_masturbation
                    /tags_public
                    /tags_orgy
                    /tags_elf
                    /tags_yuri
                    /tags_pussy
                    /tags_glasses
                    /tags_blowjob
                    /tags_handjob
                    /tags_footjob
                    /tags_boobs
                    /tags_thighs
                    /tags_ahe_gao
                    /tags_uniform
                    /tags_gangbang
                    /tags_tentacles
                    /tags_genshin_impact
                    /tags_naruto
                    /tags_bleach
                    /tags_shexyo
                    /tags_theobrobine
                    /tags_cutesexyrobutts
                    /tags_sakimichan
                    /tags_krabby_(artist)
                    /tags_lexaiduer
                    /tags_miraihikari
                    /tags_LumiNyu
                    /tags_lime_(purple_haze)
                    /tags_nepcill
                    /tags_sciamano240
                    /tags_neoartcore
                    /tags_dandon_fuga
                    /tags_zaphn
                    /tags_flou
                    /tags_prywinko
                    /tags_alexander_dinh
                    /tags_kittew
                    /tags_supullim
                    /tags_tofuubear
                    /tags_azto_dio
                    /tags_derpixon
                    /tags_kinkymation
                    /tags_ggc
                    /tags_afrobull
                    """,
            "INVALID_COMMAND", "Invalid command. For list of all supported commands, try /help",
            "INVALID_TAG", "Invalid tag. For list of all supported tags, try /help_tags",
            "NO_PICS", "No pics found for this tag. Try another one."
    );
}
