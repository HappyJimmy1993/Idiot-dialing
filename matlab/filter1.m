function y1 = filter1(filter1dir,m)
for i=1:m;
file=sprintf('%s%d.wav', filter1dir,i); 
disp(file);
amp=30;
[x,fs,nbits]=wavread(file);%语音信号采集?
N=length(x);
h=[0:N-1];                             %h为序列,n/fs为时间
X=fft(x);                              %傅里叶变换
Fs=2*fs;                               %2倍频
t=h/fs;
f=h*Fs/N;
%低通滤波器%
 %设定低通滤波器通带截止频率和阻带截止频率
wp1=2*2000/Fs; ws1=2*6000/Fs;rp=0.5;rs=100;
[N1,wp1]=ellipord(wp1,ws1,rp,rs);  %计算椭圆低通模拟滤波器的阶数和通带边界频率
[B,A]=ellip(N1,rp,rs,wp1);         %计算低通滤波器模拟滤波器系统函数系数
y1=amp*filter(B,A,x);                %滤波器软件实现
end