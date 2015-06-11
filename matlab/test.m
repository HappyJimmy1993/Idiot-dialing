function result=test(testdir, n, code)
% 说话人识别: 测试阶段
%
% 输入:
%       testdir : 测试声音文件名
%       n       : testdir中测试文件数中第N个
%       code    : 所有说话人已训练好的码书
% 注意:
%       testdir中声音文件名的格式如下: 
%               1.wav, 2.wav, ..., n.wav
%
% 例如:
%       >> test('C:\data\test\', 8, code);

              % 读取每个说话人的声音文件read test sound file of each speaker
    file = sprintf('%s%d.wav', testdir, n);
    [s, fs] = wavread(file);      
        
    v = mfcc(s,fs);            % 计算MFCC
   
    distmin = inf;              %无穷
    k1 = 0;
   
  for i = 1:length(code)      % code的最大长度，对于每个训练码书，计算失真each trained codebook, compute distortion（畸变，失真）
        d = disteu(v, code{i}); %码书 yougaidong\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
                dist = sum(min(d,[],2)) / size(d,1);%size(d,1): 列的长度即行数
 % min(A,[],dim),dim取1或2，取1时，与max(A)完全相同，取2时，该函数返回一个列向量，
 % min第i个元素是A矩阵的第i行上的最小值
      
        if dist < distmin
            distmin = dist;
           k1 = i;
        end      
end
   
    result = k1;
%     disp(msg);

