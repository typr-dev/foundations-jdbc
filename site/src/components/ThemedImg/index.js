import React from 'react';
import useIsBrowser from '@docusaurus/useIsBrowser';
import {useColorMode} from '@docusaurus/theme-common';

export default function ThemedImg({light, dark, alt, style}) {
  const isBrowser = useIsBrowser();
  const {colorMode} = useColorMode();
  const src = isBrowser && colorMode === 'dark' ? dark : light;
  return <img src={src} alt={alt} style={{borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)', maxWidth: '100%', ...style}} />;
}
