package tianma.learn.ds.string.main;


import tianma.learn.ds.Launcher.Template;
import tianma.learn.ds.Launcher.Template;

/**
 * 字符串匹配Sample
 * <p>
 * 匹配算法:<br>
 * 1. 暴力匹配<br>
 * 2. <a href="http://blog.csdn.net/v_july_v/article/details/7041827">KMP匹配 </a>
 * </br>
 * 3. 改进KMP匹配<br>
 *
 * @author Tianma
 */
public class StringMatchSample_1 {

    private interface StringMatcher {
        /**
         * 从原字符串中查找模式字符串的位置,如果模式字符串存在,则返回模式字符串第一次出现的位置,否则返回-1
         *
         * @param source  原字符串
         * @param pattern 模式字符串
         * @return if substring exists, return the first occurrence of pattern
         * substring, return -1 if not.
         */
        int indexOf(String source, String pattern);
    }

    /**
     * 暴力匹配
     * <p>
     * 时间复杂度: O(m*n), m = pattern.length, n = source.length
     */
    public static class ViolentStringMatcher implements StringMatcher {

        Template template = new Template();

        @Override
        public int indexOf(String source, String pattern) {
            int i = 0, j = 0;
            int sLen = source.length(), pLen = pattern.length();
            char[] src = source.toCharArray();
            char[] ptn = pattern.toCharArray();
            while (i < sLen && j < pLen) {
                template.instrum(
						"46",
						"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
						"WhileStatement", "i < sLen && j < pLen", "&&@<@<");
				if (src[i] == ptn[j]) {
                    template.instrum(
							"47",
							"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
							"IfStatement", "src[i] == ptn[j]", "==");
					// 如果当前字符匹配成功,则将两者各自增1,继续比较后面的字符
                    i++;
					template.instrum(
							"49",
							"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
							"PostfixExpression", "i++", "++");
                    j++;
					template.instrum(
							"50",
							"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
							"PostfixExpression", "j++", "++");
                } else {
                    // 如果当前字符匹配不成功,则i回溯到此次匹配最开始的位置+1处,也就是i = i - j + 1
                    // (因为i,j是同步增长的), j = 0;
                    i = i - j + 1;
					template.instrum(
							"54",
							"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
							"Assignment", "i=i - j + 1", "+@-@+@-=");
                    j = 0;
					template.instrum(
							"55",
							"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
							"Assignment", "j=0", "@=");
                }
            }
            // 匹配成功,则返回模式字符串在原字符串中首次出现的位置;否则返回-1
            if (j == pLen) {
                template.instrum(
						"59",
						"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf",
						"IfStatement", "j == pLen", "==");
				return i - j;
            } else {
                return -1;
            }
        }
    }

    /**
     * KMP模式匹配
     *
     * @author Tianma
     */
    public static class KMPStringMatcher implements StringMatcher {

        /**
         * 获取KMP算法中pattern字符串对应的next数组
         *
         * @param p 模式字符串对应的字符数组
         * @return
         */
        protected int[] getNext(char[] p) {

            Template template = new Template();
            // 已知next[j] = k,利用递归的思想求出next[j+1]的值
            // 如果已知next[j] = k,如何求出next[j+1]呢?具体算法如下:
            // 1. 如果p[j] = p[k], 则next[j+1] = next[k] + 1;
            // 2. 如果p[j] != p[k], 则令k=next[k],如果此时p[j]==p[k],则next[j+1]=k+1,
            // 如果不相等,则继续递归前缀索引,令 k=next[k],继续判断,直至k=-1(即k=next[0])或者p[j]=p[k]为止
            int pLen = p.length;
            int[] next = new int[pLen];
            int k = -1;
            int j = 0;
            next[0] = -1; // next数组中next[0]为-1
			template.instrum(
					"92",
					"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
					"Assignment", "next[0]=-1", "@=");
            while (j < pLen - 1) {
                template.instrum(
						"93",
						"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
						"WhileStatement", "j < pLen - 1", "<@-");
				if (k == -1 || p[j] == p[k]) {
                    template.instrum(
							"94",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
							"IfStatement", "k == -1 || p[j] == p[k]",
							"||@==@==");
					k++;
					template.instrum(
							"95",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
							"PostfixExpression", "k++", "++");
                    j++;
					template.instrum(
							"96",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
							"PostfixExpression", "j++", "++");
                    next[j] = k;
					template.instrum(
							"97",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
							"Assignment", "next[j]=k", "@=");
                } else {
                    k = next[k];
					template.instrum(
							"99",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.getNext",
							"Assignment", "k=next[k]", "@=");
                }
            }
            return next;
        }

        @Override
        public int indexOf(String source, String pattern) {

            Template template = new Template();
            int i = 0, j = 0;
            char[] src = source.toCharArray();
            char[] ptn = pattern.toCharArray();
            int sLen = src.length;
            int pLen = ptn.length;
            int[] next = getNext(ptn);
            while (i < sLen && j < pLen) {
                template.instrum(
						"115",
						"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.indexOf",
						"WhileStatement", "i < sLen && j < pLen", "&&@<@<");
				// 如果j = -1,或者当前字符匹配成功(src[i] = ptn[j]),都让i++,j++
                if (j == -1 || src[i] == ptn[j]) {
                    template.instrum(
							"117",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.indexOf",
							"IfStatement", "j == -1 || src[i] == ptn[j]",
							"||@==@==");
					i++;
					template.instrum(
							"118",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.indexOf",
							"PostfixExpression", "i++", "++");
                    j++;
					template.instrum(
							"119",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.indexOf",
							"PostfixExpression", "j++", "++");
                } else {
                    // 如果j!=-1且当前字符匹配失败,则令i不变,j=next[j],即让pattern模式串右移j-next[j]个单位
                    j = next[j];
					template.instrum(
							"122",
							"tianma.learn.ds.string.main.StringMatchSample_1.KMPStringMatcher.indexOf",
							"Assignment", "j=next[j]", "@=");
                }
            }
            if (j == pLen)
                return i - j;
            return -1;
        }
    }

    /**
     * 优化的KMP算法(对next数组的获取进行优化)
     *
     * @author Tianma
     */
    public static class OptimizedKMPStringMatcher extends KMPStringMatcher {

        @Override
        protected int[] getNext(char[] p) {

            Template template = new Template();
            // 已知next[j] = k,利用递归的思想求出next[j+1]的值
            // 如果已知next[j] = k,如何求出next[j+1]呢?具体算法如下:
            // 1. 如果p[j] = p[k], 则next[j+1] = next[k] + 1;
            // 2. 如果p[j] != p[k], 则令k=next[k],如果此时p[j]==p[k],则next[j+1]=k+1,
            // 如果不相等,则继续递归前缀索引,令 k=next[k],继续判断,直至k=-1(即k=next[0])或者p[j]=p[k]为止
            int pLen = p.length;
            int[] next = new int[pLen];
            int k = -1;
            int j = 0;
            next[0] = -1; // next数组中next[0]为-1
			template.instrum(
					"151",
					"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
					"Assignment", "next[0]=-1", "@=");
            while (j < pLen - 1) {
                template.instrum(
						"152",
						"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
						"WhileStatement", "j < pLen - 1", "<@-");
				if (k == -1 || p[j] == p[k]) {
                    template.instrum(
							"153",
							"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
							"IfStatement", "k == -1 || p[j] == p[k]",
							"||@==@==");
					k++;
					template.instrum(
							"154",
							"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
							"PostfixExpression", "k++", "++");
                    j++;
					template.instrum(
							"155",
							"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
							"PostfixExpression", "j++", "++");
                    // 修改next数组求法
                    if (p[j] != p[k]) {
                        template.instrum(
								"157",
								"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
								"IfStatement", "p[j] != p[k]", "!=");
						next[j] = k;// KMPStringMatcher中只有这一行
						template.instrum(
								"158",
								"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
								"Assignment", "next[j]=k", "@=");
                    } else {
                        // 不能出现p[j] = p[next[j]],所以如果出现这种情况则继续递归,如 k = next[k],
                        // k = next[[next[k]]
                        next[j] = next[k];
						template.instrum(
								"162",
								"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
								"Assignment", "next[j]=next[k]", "@=");
                    }
                } else {
                    k = next[k];
					template.instrum(
							"165",
							"tianma.learn.ds.string.main.StringMatchSample_1.OptimizedKMPStringMatcher.getNext",
							"Assignment", "k=next[k]", "@=");
                }
            }
            return next;
        }

    }

    public static void main(String[] args) {

        Template template = new Template();
        StringMatcher matcher = new ViolentStringMatcher();
        System.out.println(matcher.indexOf("Reena Mary Puthota", "Reena"));
        matcher = new KMPStringMatcher();
        System.out.println(matcher.indexOf("Sai Sharan Nagulapalli", "Sharan"));
        matcher = new OptimizedKMPStringMatcher();
        System.out.println(matcher.indexOf("Tathagata Ganguly", "Ganguly"));
    }

}
