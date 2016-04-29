function [Legend, MatrixInput] = stat(fileNameWithSuffix)
fileID = fopen(fileNameWithSuffix, 'r');

MatrixInput = [];
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
    
    % create one matrix row from one input line
    RowM = [rowFIterator];
    for colFIterator=1:colFCountI
        valII = rowNumValuesI(colFIterator);
        RowM=[RowM valII]; % append column
    end
    
    MatrixInput=[MatrixInput; RowM]; % append row
    
    rowFIterator = rowFIterator + 1;
    tline = fgets(fileID);
end

% removes last empty column from legend
Legend = Legend(:,1:end-1);

fclose(fileID);
end
