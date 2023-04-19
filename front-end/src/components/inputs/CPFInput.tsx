import React from 'react';
import { PatternFormat, type PatternFormatProps } from 'react-number-format';

interface CustomProps {
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

export const CPFInput = React.forwardRef<PatternFormatProps, CustomProps>(function Cpf(props, ref) {
  const { onChange, ...other } = props;

  return (
    <PatternFormat
      {...other}
      getInputRef={ref}
      onValueChange={(values) => {
        onChange({
          target: {
            name: props.name,
            value: values.value,
          },
        });
      }}
      mask="_"
      format="###.###.###-##"
    />
  );
});
