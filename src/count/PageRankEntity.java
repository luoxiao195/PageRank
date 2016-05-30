package count;

import java.util.ArrayList;

import craw.WebEntity;

public class PageRankEntity {
	public double num[][];				//�ڽӾ���
	public double A_T[][];				//���۾���
	public double v[];					//pagerank����
	
	private double d;					//��������
	private int length;					//���ݳ���
	private double e[];					//E����
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public double[][] getNum() {
		return num;
	}
	public void setNum(double[][] num) {
		this.num = num;
	}
	//��ʼ������
	public PageRankEntity(int len){
		this.num=new double [len+1][len+1];
		this.A_T=new double [len+1][len+1];
		this.d=0.85;
		this.length=len+1;
		this.v=new double[len+1];
		this.e=new double[len+1];
		
		for(int i=0;i<len+1;i++){
			v[i]=(double)(1.0/len);
			e[i]=(double)(1.0/len);
		}
		for(int i=0;i<len+1;i++){
			for(int j=0;j<len+1;j++)
				num[i][j]=0;
		}
	}
	//Ϊ���۾���ֵ
	public void giveA_T(){
		double count=0;
		for(int i=0;i<length;i++){
			for(double t:num[i]){
				if(t==1)
					count++;
			}
			for(int j=0;j<length;j++){
				if(num[i][j]==1)
					A_T[j][i]=num[i][j]/count;
				else
					A_T[j][i]=0;
			}
			count=0;
		}
	}
	
	public void showNum(){
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				System.out.print(num[i][j]+" ");
			}
			System.out.println("");
		}
	}
	public void showA_T(){
		for(int i=0;i<length;i++){
			for(int j=0;j<length;j++){
				System.out.print(A_T[i][j]+" ");
			}
			System.out.println("");
		}
	}
	//����pageRank��ֵ
	public void alg(){
		double tmp_V[]=new double[length];
		double sum_aNV=0;
		double sum_aE=0;
		double sum_tmp=100;
		while(sum_tmp>0.001){
			System.arraycopy(v, 0, tmp_V, 0, length);
			
			for(int i=0;i<length;i++){
				sum_aNV=0;
				sum_aE=0;
				sum_tmp=0;
				for(int j=0;j<length;j++){
					sum_tmp+=(A_T[i][j]*tmp_V[j]);
				}
				sum_aNV=sum_tmp*d;
				sum_aE=(1-d)*e[i];
				
				v[i]=sum_aNV+sum_aE;
				
			}
			sum_tmp=0;
			for(int i=0;i<length;i++){
				sum_tmp+=Math.abs(tmp_V[i]-v[i]);
			}
			
		}
	}
	public void show_V(ArrayList<WebEntity> retList){
		for(int i=0;i<length-2;i++){
			System.out.print(retList.get(i).Url.toString()+"  PageRank:");
			System.out.println(v[i]);
		}
			
	}
	public void sort_v(){
		double tmp=0;int pos=0;
		for(int i=0;i<length;i++){
			pos=i;
			for(int j=i+1;j<length;j++){
				if(v[pos]<v[j])
					pos=j;
			}
			if(pos!=i){
				tmp=v[pos];
				v[pos]=v[i];
				v[i]=tmp;
			}
		}
	}
	//ִ�к����ļ���
	public void PageRank(){
		giveA_T();
		alg();
		
		sort_v();
		System.out.println("sort the pagerank");
	}
}
