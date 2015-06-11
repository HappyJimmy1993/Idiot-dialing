function y1=cut(s_address)
%�������źŴ�¼���ļ��м��г���
% s_address �����ļ��ĵ�ַ
y=wavread(s_address);
h=hamming(256);

% �����ʱƽ������SAE��short average energe��
% E(n)��[x(m)]^2*h(n-m),m�Ӹ������������ͣ�h(n-m)Ϊ������
e=conv(y.*y,h);    % y.*2��y�и�Ԫ��ƽ����conv(u,v) ��u��v�ľ��

% �������źŽ����и��SAEС��������ֵ��1/100ʱ����Ϊ�������յ�

mx=max(e);
n=length(e);
y(n)=0; % ��ԭʼ�����źž���������nά
for i=1:n
    if e(i)<mx*0.01
        e(i)=0;
    else e(i)=1;    % e�з�0������1������
    end
end
y1=y.*e;
y1(find(y1==0))=[]; % ��0Ԫ���޳�
% fs=11025;
% wavwrite(y1,fs,'D:\Program Files\MATLAB704\work\speaker\cut\y1.wav');