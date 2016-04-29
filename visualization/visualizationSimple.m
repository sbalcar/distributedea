
disp('DistributedEA - Simple result visualization');


START_ROW_NUMBER = 10;
END_ROW_NUMBER = 500;
INPUT_FILE_NAME  = '/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/result/results0.txt';
OUTPUT_FILE_NAME = '/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/visualization/visualization.jpg';


fileID = fopen(INPUT_FILE_NAME, 'r');


% array of method(agent names) for graph description
Legend=[];

% matrix of Input values
MatrixInput=[];

% reads file with results to Legend and Matrix
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
    MatrixInput(rowFIterator, 1) = rowFIterator;
    
    for colFIterator=1:colFCountI
        valII = rowNumValuesI(colFIterator);
        MatrixInput(rowFIterator, colFIterator +1) = valII;
    end
    
    rowFIterator = rowFIterator +1;
    tline = fgets(fileID);
end

fclose(fileID);

% prints legend to command line
disp('This are legends:');
disp(Legend);

% print matrix of values to command lines
disp('This are input values:');
disp(MatrixInput);



% matrix of Transformed values
MatrixTransformed=[];

% takes part of matrix which will be printed
for rowMIterator=START_ROW_NUMBER:END_ROW_NUMBER
    roxI = MatrixInput(rowMIterator,:);
    MatrixTransformed = [MatrixTransformed; roxI];
end



h = figure
hold on
title('Fitness TSP ostrovů v závislosti na čase')
xlabel('x: čas v sekundách', 'FontSize', 10);
ylabel('y: hodnota fitnes v kilometrech', 'FontSize', 10);

colMCount = size(MatrixTransformed, 2);

for colMIterator=1:colMCount-1
    
    % prints value to picture 
    xValI=MatrixTransformed(:,1);
    yValI=MatrixTransformed(:,colMIterator +1);
    pyI=plot(xValI, yValI);
    
    % prints legend to picture 
    gLeg=regexprep(Legend(colMIterator +1),'[_]','\\_');
    legend(pyI, gLeg);
end


legend(gca,'off');    
legend('show');

hold off

saveas(h, OUTPUT_FILE_NAME);

