package com.os4.ecb.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.os4.ecb.R;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

public class EmoticonGenerator {

	private static final Factory spannableFactory = Spannable.Factory.getInstance();
	private static final Map<Integer,String> emoticonCodes = new HashMap<Integer, String>();
	private static final Map<Integer,String> stickerCodes = new HashMap<Integer, String>();
	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();
	
	static {
		addEmoticon(emoticonCodes, "1:)", R.drawable.sic_aha);
		addEmoticon(emoticonCodes, "2:)", R.drawable.sic_awas_ya);
		addEmoticon(emoticonCodes, "3:)", R.drawable.sic_bandit);
		addEmoticon(emoticonCodes, "4:)", R.drawable.sic_bapak_galak);
		addEmoticon(emoticonCodes, "5:)", R.drawable.sic_cape);
		addEmoticon(emoticonCodes, "6:)", R.drawable.sic_cengdem);
		addEmoticon(emoticonCodes, "7:)", R.drawable.sic_emo);
		addEmoticon(emoticonCodes, "8:)", R.drawable.sic_hehehe);
		addEmoticon(emoticonCodes, "9:)", R.drawable.sic_heran);
		addEmoticon(emoticonCodes, ":D", R.drawable.sic_hihihi);
		addEmoticon(emoticonCodes, "11:)", R.drawable.sic_hmm);
		addEmoticon(emoticonCodes, "12:)", R.drawable.sic_kaget);
		addEmoticon(emoticonCodes, "13:)", R.drawable.sic_ketawa);
		addEmoticon(emoticonCodes, "14:)", R.drawable.sic_malu);
		addEmoticon(emoticonCodes, "15:)", R.drawable.sic_marah);
		addEmoticon(emoticonCodes, "16:)", R.drawable.sic_melas);
		addEmoticon(emoticonCodes, "17:)", R.drawable.sic_mikir);
		addEmoticon(emoticonCodes, "18:)", R.drawable.sic_mmm);
		addEmoticon(emoticonCodes, "19:)", R.drawable.sic_nangis);
		addEmoticon(emoticonCodes, "20:)", R.drawable.sic_ngambek);
		addEmoticon(emoticonCodes, "21:)", R.drawable.sic_ngantuk);
		addEmoticon(emoticonCodes, "22:)", R.drawable.sic_ngelamun);
		addEmoticon(emoticonCodes, "23:)", R.drawable.sic_nguap);
		addEmoticon(emoticonCodes, "24:)", R.drawable.sic_omg);
		addEmoticon(emoticonCodes, "25:)", R.drawable.sic_sedih);
		addEmoticon(emoticonCodes, "26:)", R.drawable.sic_senang);
		addEmoticon(emoticonCodes, ":)", R.drawable.sic_senyum);
		addEmoticon(emoticonCodes, "28:)", R.drawable.sic_seuri);
		addEmoticon(emoticonCodes, "29:)", R.drawable.sic_sinis);
		addEmoticon(emoticonCodes, "30:)", R.drawable.sic_takut);
		addEmoticon(emoticonCodes, "31:)", R.drawable.sic_tengil);
		addEmoticon(emoticonCodes, "32:)", R.drawable.sic_tersinggung);
		addEmoticon(emoticonCodes, "33:)", R.drawable.sic_thumb_up);
		addEmoticon(emoticonCodes, "34:)", R.drawable.sic_tidur);
		addEmoticon(emoticonCodes, "35:)", R.drawable.sic_tongue);
		addEmoticon(emoticonCodes, "36:)", R.drawable.sic_winking);
		addEmoticon(emoticonCodes, "(+)", android.R.drawable.ic_menu_add);
		loadEmoticon();
	}

	public static void addEmoticon(Map<Integer,String> map, String code,int resource) {
	    map.put(resource,code);
	}

	public static void loadEmoticon(){
		for (Entry<Integer, String> entry : emoticonCodes.entrySet()) {
			addPattern(emoticons, entry.getValue(), entry.getKey());
		}
	}
	
	public static void addPattern(Map<Pattern, Integer> map, String code,int resource) {
	    map.put(Pattern.compile(Pattern.quote(code)), resource);
	}
	
	public static Map<Integer, String> getEmoticonResources(){
		return emoticonCodes;
	}

	public static Map<Integer, String> getStickerResources(){
		return stickerCodes;
	}
	
	public static String getPatern(Integer resource){
		return emoticonCodes.get(resource);
	}
	
	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue()),matcher.start(), matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}
	
	public static Spannable getEmoticonText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	public static Spannable putEmoticonText(Context context, CharSequence text, int resource) {
	    Spannable spannable = spannableFactory.newSpannable(text + " ");
	    spannable.setSpan(new ImageSpan(context, resource),text.length(), text.length() + 1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    return spannable;
	}	
}
