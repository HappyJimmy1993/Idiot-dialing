function c=mfcc(s,fs)   % 创建函数mfcc，其中，c为输出变量，mfcc为函数名，s、fs为输入变量；
% s 输入信号
% n 每个帧的采样点数
% m 相邻帧起点之间距离

% close all,clc,clear;
% 下面这些为什么都是注释？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
% [filename,filepath]=uigetfile('*.wav','输入一个语音信号')   返回指定文件类型的名称和路径的对话框；
% signal=strcat(filepath,filename);  连接字符串，直接连接，中间没有空开；？？？？?为什么
% [s,fs,bits]=wavread(signal);   %将一个语音文件采样；
% t=length(s)/fs;   % 语音信号时间长度；计算t意在做什么？？？？？？？？？？？语音信号本身就是时域信号；
%sound(s，10000，16);%sound(y,fs,bits) % 将向量转换成声音；为什么有这一步，其目的是什么？？？？？？？？？？？？？？？？？？？？？？
%figure(1),plot(s),title('original signal')

%预加重
a=0.98;
len=length(s);
for i=2:len
    s1(i)=s(i)-a*s(i-1);%形成了一个新的信号s1；
end
%figure(2),plot(s1),title('heavyed signal');  s1为预加重后的信号；

%计算功率谱密度

n=256;%每个帧的采样点数
m=100;%相邻帧起点之间距离帧与帧之间的偏移；
% [Pxx,w]=pwelch(s1,n,m,256,10000);  这个函数是计算括号内所描述函数的功率谱密度；
% figure(3),subplot(2,2,1),plot(w,Pxx),title(['n=',num2str(n),'
% m=',num2str(m)]);  这些为什么都在注释里面？？？？？？？？？？？？？？？？？？？？？？

%分帧
frame=floor((len-n)/m)+1;%信号分帧的个数，floor的作用是取接近于括号内A的整数；
for j=1:frame %一列为一帧
    for i=1:n
        z(i,j)=s1((j-1)*m+i);  %这句话对吗，s1不是从2开始的吗？1
    end
end

% z1=z(:);什么意思嘛？？？？？？？？？？？？？？？33024=256*129；？？？？？？？？？？？？？？？？？？？？2

%figure(3),plot(z1),title('frame')为什么是注释？？？？？？为什么这里又是图3，跟上面一样？？？？？？？

%加窗hamming
h=hamming(n);
for j=1:frame
    for i=1:n
        z2(i,j)=h(i)*z(i,j);%加窗；
    end
end
% z3=z2(:);同上
% figure(4),plot(z3),title('window')   

% fft变换
for j=1:frame
    FFT(:,j)=fft(z2(:,j));%每一帧都要进行傅氏变换；
end
% ps=FFT.*conj(FFT); %power spectrum能量谱；与其复共轭相乘，即取模的平方；语音信号的幅度平方谱；
% figure(5),imagesc(ps);

%melfb 生成mel域滤波器组；
m=melfb(20,n,fs);     % 这里应该是调用melfb函数，这里取p=20，指滤波器个数；m是指什么？？？？？？？？？？？
n2=1+floor(n/2);
mel=m*abs(FFT(1:n2,:)).^2; %计算经mel滤波器组加权后的能量值；abs(FFT(1:n2,:)).^2为能量谱，幅度平方谱通过美尔滤波器组；
                            % *m为通过一组美尔尺度的三角形滤波器组，得到经滤波器组加权后的能量值；
c=dct(log(mel));  %  将滤波器组的输出取对数，然后做DCT变换；得到mel倒谱系数；
c(1,:)=[];  %去除c的第一行；

% 过程总结：输入语音-预加重-分帧-加窗-FFT-经MEL滤波器组频响加权-计算加权后的能量值-将输出取对数，做DCT变换-得到mel倒谱系数；


