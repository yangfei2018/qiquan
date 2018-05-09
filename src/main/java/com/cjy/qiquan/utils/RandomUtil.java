/**
 * 
 */
package com.cjy.qiquan.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.util.StringUtils;

public class RandomUtil
{
	private static final Random random = new Random();
	 /**
     * 字母和数字的集合
     */
    private static final char[] LetterAndNumber = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',  'm', 'n', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

    
    private static final char[] NumberOnly = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
    
    
    /**
     * 字母集合
     */
    private static final char[] LetterOnly      = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z'            };

	private RandomUtil()
	{

	}

	
	public static int getRandomOfUrl()
	{
		return random.nextInt() % 100;
	}

	public static <T> T getRandomElment(List<T> list)
	{
		if (list == null) throw new IllegalArgumentException("list is null");

		Integer size = list.size();

		if (size == 0) return null;

		int index = Math.abs(random.nextInt()) % size;

		return list.get(index);
	}
	public static int getRandomValue(int value){
		return random.nextInt(value);
	}
	
	public static int getRandomValue(int min,int max)
	{
		return Math.abs(random.nextInt(max+1-min))+ min;
	}
	
	public static int nextFloatToUpInt(float f){
		int roll=random.nextInt( new Double(f*10).intValue()+1);
		if(roll==0)
			return 1;
		return new Double(Math.ceil(roll/10.0)).intValue();
	}
	
	
	public static float nextFloat(float min,float max){
		int minInt = new Double(Math.ceil(min * 1000)).intValue();
		int maxInt = new Double(Math.ceil(max * 1000)).intValue();
		int r = getRandomValue(minInt, maxInt);
		return (float)r / 1000.f;
	}
	
	/**
	 * 
	 * @param num
	 * @param range
	 * @return
	 */
	public static int[] randomSeeds(int num,int range){
		int[] seeds = new int[num];
		for(int i=0;i<num;i++){
			seeds[i]=RandomUtil.getRandomValue(0,range);
		}
		
		return seeds;
	}
	
	
	public static String randomSeedsToString(int num,int range){
		int[] seeds = randomSeeds(num,range);
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<seeds.length;i++){
			if (StringUtils.hasText(sb.toString())){
				sb.append("_"+seeds[i]);
			}else{
				sb.append(seeds[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 从给定的值中随机几个出来，eg:从5个里面随机3个值
	 * @param getLength---3
	 * @param randomMax---5
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  static  Set  getObjectRandom(int  getLength,int  randomMax)
	{
		  Set set = new HashSet();
		  if(randomMax<getLength){
			  return null;
		  }
			while(true){
				if(set.size()>=getLength){
					break;
				}else{
					Integer  randType = getRandomValue(0,randomMax-1);
					set.add(randType);		
				}
			}
			return  set;
	}
	
    /**
     * 得到指定长度的含字母或数字的随机字符串（总是以字母开头）
     * @param length 指定长度
     * @return String 随机字符串
     */
    public static String getRandomTxt(int length) {
        StringBuilder sb = new StringBuilder(length);
        sb.append(LetterAndNumber[getRandomValue(LetterAndNumber.length)]);
        for (int i = length - 1; i > 0; i--) {
            sb.append(LetterAndNumber[getRandomValue(LetterAndNumber.length)]);
        }
        return sb.toString();
    }
    
    public static String getRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        sb.append(NumberOnly[getRandomValue(NumberOnly.length)]);
        for (int i = length - 1; i > 0; i--) {
            sb.append(NumberOnly[getRandomValue(NumberOnly.length)]);
        }
        return sb.toString();
    }
    
    public static String getRandomLetter(int length) {
        StringBuilder sb = new StringBuilder(length);
        sb.append(LetterOnly[getRandomValue(LetterOnly.length)]);
        for (int i = length - 1; i > 0; i--) {
            sb.append(LetterOnly[getRandomValue(LetterOnly.length)]);
        }
        return sb.toString();
    }
    
    
	public static void main(String[] a){
		
		System.out.println(CipherUtil.generatePassword("12345"));
		
	}
	
}
