package com.moez.QKSMS.common.formatter;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.moez.QKSMS.QKSMSAppBase;
import com.moez.QKSMS.data.Contact;
/**
 * Created by FEILONG on 2017-12-03.
 */

public class FeiLongSMSFormat implements Formatter {
    String mCountryIso;

    private String RegexStr[][] = {
            //搜索关键字，匹配正则，替换表达式

            //工行
            //您尾号1234卡12月3日12:34工商银行支出(银联)12.34元，余额1234.56元。【工商银行】
            {
                ".*尾号(\\d+)[^\\d]+((\\d+)月(\\d+)日).*支出([^\\d]+)(\\d+(?:\\.\\d\\d))元.*余额(\\d+(?:\\.\\d\\d)?)元.*【工商银行】",
                ".*尾号(\\d+)[^\\d]+((\\d+)月(\\d+)日).*支出([^\\d]+)(\\d+(?:\\.\\d\\d))元.*余额(\\d+(?:\\.\\d\\d)?)元.*【工商银行】",
                "工商银行卡：$1\n" +
                        "消费原因：$5\n" +
                        "消费时间：$2\n" +
                        "消费金额：$6元\n" +
                        "银行卡余额：$7元"
            },
            //中国民航大学校园卡
            //已成功从支付宝账户614***.com扣除100元到校园一卡通账户（**4238），请务必前往学校消费机消费或圈存机领取。【支付宝】
            {
                "已成功从支付宝账户([0-9a-zA-Z\\*\\.]+)扣除(\\d+)元到校园一卡通账户（(.*)），请务必前往学校消费机消费或圈存机领取。【支付宝】",
                    "已成功从支付宝账户([0-9a-zA-Z\\*\\.]+)扣除(\\d+)元到校园一卡通账户（(.*)），请务必前往学校消费机消费或圈存机领取。【支付宝】",
                    "校园卡被充值了！\n" +
                            "支付宝：$1\n" +
                            "一卡通：$3\n" +
                            "金额：$2元"
            },
            //韵达快递
            //【韵达快递】亲，您的运单3974480293997已为您放在南院大门口，请现在或下课来取，我1点走，当天请务必当天取走不，不取安问件退回处理，谢谢合作，有问题请联系快递员丁翠，手机号：15222564247。
            {
                "【韵达快递】亲，您的运单(\\d+)已为您放在(\\w+)，请现在或下课来取，我(\\w+)走，当天请务必当天取走不，不取安问件退回处理，谢谢合作，有问题请联系快递员(\\w+)，手机号：(\\d{11})。",
                    "【韵达快递】亲，您的运单(\\d+)已为您放在(\\w+)，请现在或下课来取，我(\\w+)走，当天请务必当天取走不，不取安问件退回处理，谢谢合作，有问题请联系快递员(\\w+)，手机号：(\\d{11})。",
                    "韵达快递\n" +
                            "时间：$3\n" +
                            "地点：$2\n" +
                            "运单号：$1\n" +
                            "派件员：$4\n" +
                            "联系方式：$5"
            },
            //狗东
            //（还款提醒）您本期白条账单将于 12月5日从还款账户自动扣款 17.50元。账单分期抽拉杆箱，去分期 3.cn/GhDFmX\n【京东金融】
            {
                    "（还款提醒）您本期白条账单将于 (\\d+月\\d+日)从还款账户自动扣款 (\\d+(?:\\.\\d{2}))元。账单分期抽拉杆箱，去分期 3.cn/GhDFmX\n" +
                            "【京东金融】",
                    "（还款提醒）您本期白条账单将于 (\\d+月\\d+日)从还款账户自动扣款 (\\d+(?:\\.\\d{2}))元。账单分期抽拉杆箱，去分期 3.cn/GhDFmX\n" +
                            "【京东金融】",
                    "京东金融还款提醒\n" +
                            "时间：$1\n" +
                            "价格：$2元"
            },
            //联通
            //温馨提示：截止11月29日24时，您当月累计使用流量126.2MB。其中：国内流量已使用126.2MB，剩余1.693GB； 回复“流量查询”或点击进入http://wap.10010.com 查询详情。
            {
                "温馨提示：截止(\\d+月\\d+日\\d+时)，您当月累计使用流量([\\.\\w]+)B。其中：国内流量已使用([\\.\\w]+)B，剩余([\\.\\w]+)B； 回复“流量查询”或点击进入http://wap.10010.com 查询详情。",
                    "温馨提示：截止(\\d+月\\d+日\\d+时)，您当月累计使用流量([\\.\\w]+)B。其中：国内流量已使用([\\.\\w]+)B，剩余([\\.\\w]+)B； 回复“流量查询”或点击进入http://wap.10010.com 查询详情。",
                    "流量提醒\n" +
                            "剩余流量：$4\n" +
                            "已用流量：$2\n" +
                            "国内已用：$3\n" +
                            "截止时间：$1"
            },
            //cauc驿站
            //【菜鸟驿站】您的12月2日中通包裹等您很久了，请凭（22-2-8972）尽快到中国民航大学南院易天驿站菜鸟驿站领取，谢谢理解
            {
              "【菜鸟驿站】您的(\\d+月\\d+日)(\\w+)包裹等您很久了，请凭（([\\d\\-]+)）尽快到中国民航大学(\\w{2})易天驿站菜鸟驿站领取，谢谢理解",
                    "【菜鸟驿站】您的(\\d+月\\d+日)(\\w+)包裹等您很久了，请凭（([\\d\\-]+)）尽快到中国民航大学(\\w{2})易天驿站菜鸟驿站领取，谢谢理解",
                    "学校驿站\n" +
                            "编号：$3\n" +
                            "快递：$2\n" +
                            "地点：$4\n" +
                            "到达时间：$1\n"
            },
            //【菜鸟驿站】中通包裹到中国民航大学南院易天驿站菜鸟驿站，请18:00前凭22-2-8972取，联系方式： m.tb.cn/E.M8KZr。
            {
                "【菜鸟驿站】(中通)包裹到中国民航大学(南院)易天驿站菜鸟驿站，请(\\d+:\\d+)前凭([\\d\\-]+)取.*",
                    "【菜鸟驿站】(中通)包裹到中国民航大学(南院)易天驿站菜鸟驿站，请(\\d+:\\d+)前凭([\\d\\-]+)取.*"
                    "学校驿站\n" +
                            "编号：$4\n" +
                            "快递：$1\n" +
                            "地点：$2\n" +
                            "关门时间：$3"
            }

    };

    private String ReplaceText(String text,String regex,String replace)
    {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.replaceAll(replace);
    }

    @Override
    public String format(String text) {
        for (String[] regex : RegexStr){
            try {
                text = text.matches(regex[0])?ReplaceText(text,regex[1],regex[2]):text;
            }
            catch(Exception ex){
                text = "regex error!\n" + regex[0];
                Log.e("feilong",ex.getMessage());
                Log.e("feilong",regex[0]);
            }
        }

        return text;
    }
}
