
disp('DistributedEA result visualization');

fileID = fopen('/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/results.txt','r');

Legend=[];

rowFIterator = 1;
tline = fgets(fileID);
while ischar(tline)

    if regexp(tline, '^#')
        Legend = strsplit(tline);
        tline = fgets(fileID);
        continue;
    end
    
    rowNumValuesI = str2num(tline);
    
    colFCountI = size(rowNumValuesI, 2);
    Matrix(rowFIterator, 1) = rowFIterator;
    
    for colFIterator=1:colFCountI
        valII = rowNumValuesI(colFIterator);
        Matrix(rowFIterator, colFIterator +1) = valII;
    end
    
    rowFIterator = rowFIterator +1;
    tline = fgets(fileID);
end

fclose(fileID);

disp(Legend);
disp(Matrix);
colMCount = size(Matrix, 2);


h = figure
hold on
title('Fitness TPS ostrovů v závislosti na čase')
xlabel('x: čas v sekundách', 'FontSize', 10);
ylabel('y: hodnota fitnes v kilometrech', 'FontSize', 10);

for colMIterator=1:colMCount-1
    
    xValI=Matrix(:,1);
    yValI=Matrix(:,colMIterator +1);
    pyI=plot(xValI, yValI);
    
    %legend(pyI, ['agent ', num2str(colMIterator)] );
    gLeg=regexprep(Legend(colMIterator +1),'[_]','\\_');
    legend(pyI, gLeg);
end


legend(gca,'off');    
legend('show');

hold off

saveas(h, '/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/visualization/visualization.jpg');

