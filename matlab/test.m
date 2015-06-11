function result=test(testdir, n, code)
% ˵����ʶ��: ���Խ׶�
%
% ����:
%       testdir : ���������ļ���
%       n       : testdir�в����ļ����е�N��
%       code    : ����˵������ѵ���õ�����
% ע��:
%       testdir�������ļ����ĸ�ʽ����: 
%               1.wav, 2.wav, ..., n.wav
%
% ����:
%       >> test('C:\data\test\', 8, code);

              % ��ȡÿ��˵���˵������ļ�read test sound file of each speaker
    file = sprintf('%s%d.wav', testdir, n);
    [s, fs] = wavread(file);      
        
    v = mfcc(s,fs);            % ����MFCC
   
    distmin = inf;              %����
    k1 = 0;
   
  for i = 1:length(code)      % code����󳤶ȣ�����ÿ��ѵ�����飬����ʧ��each trained codebook, compute distortion�����䣬ʧ�棩
        d = disteu(v, code{i}); %���� yougaidong\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                dist = sum(min(d,[],2)) / size(d,1);%size(d,1): �еĳ��ȼ�����
 % min(A,[],dim),dimȡ1��2��ȡ1ʱ����max(A)��ȫ��ͬ��ȡ2ʱ���ú�������һ����������
 % min��i��Ԫ����A����ĵ�i���ϵ���Сֵ
      
        if dist < distmin
            distmin = dist;
           k1 = i;
        end      
end
   
    result = k1;
%     disp(msg);

