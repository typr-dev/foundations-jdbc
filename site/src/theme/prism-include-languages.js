import siteConfig from '@generated/docusaurus.config';

export default function prismIncludeLanguages(PrismObject) {
  const {
    themeConfig: {prism},
  } = siteConfig;
  const {additionalLanguages} = prism;

  const PrismBefore = globalThis.Prism;
  globalThis.Prism = PrismObject;

  additionalLanguages.forEach((lang) => {
    if (lang === 'php') {
      require('prismjs/components/prism-markup-templating.js');
    }
    require(`prismjs/components/prism-${lang}`);
  });

  // Enhance Scala highlighting: the default grammar deliberately removes
  // class-name, function, and constant patterns. Add them back with
  // Scala-appropriate rules.
  if (PrismObject.languages.scala) {
    PrismObject.languages.scala['class-name'] = {
      pattern: /(\b(?:class|trait|object|enum|extends|with|new|type)\s+)[A-Z]\w*/,
      lookbehind: true,
    };
    PrismObject.languages.insertBefore('scala', 'keyword', {
      'type-name': {
        pattern: /\b[A-Z]\w*(?:\[[\w\[\], ?]*\])?/,
        alias: 'class-name',
      },
    });
    PrismObject.languages.scala['function'] = {
      pattern: /(\bdef\s+)\w+/,
      lookbehind: true,
    };
  }

  delete globalThis.Prism;
  if (typeof PrismBefore !== 'undefined') {
    globalThis.Prism = PrismObject;
  }
}
