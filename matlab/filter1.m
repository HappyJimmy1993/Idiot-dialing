function y1 = filter1(filter1dir,m)
for i=1:m;
file=sprintf('%s%d.wav', filter1dir,i); 
disp(file);
amp=30;
[x,fs,nbits]=wavread(file);%�����źŲɼ�?
N=length(x);
h=[0:N-1];                             %hΪ����,n/fsΪʱ��
X=fft(x);                              %����Ҷ�任
Fs=2*fs;                               %2��Ƶ
t=h/fs;
f=h*Fs/N;
%��ͨ�˲���%
 %�趨��ͨ�˲���ͨ����ֹƵ�ʺ������ֹƵ��
wp1=2*2000/Fs; ws1=2*6000/Fs;rp=0.5;rs=100;
[N1,wp1]=ellipord(wp1,ws1,rp,rs);  %������Բ��ͨģ���˲����Ľ�����ͨ���߽�Ƶ��
[B,A]=ellip(N1,rp,rs,wp1);         %�����ͨ�˲���ģ���˲���ϵͳ����ϵ��
y1=amp*filter(B,A,x);                %�˲������ʵ��
end