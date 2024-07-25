function analyzeContent(content) {
    // Helper function to count occurrences of tags or CSS targets
    function countOccurrences(pattern, content) {
        const counts = {};
        let match;
        while ((match = pattern.exec(content)) !== null) {
            // Ensure match[1] is defined before calling toLowerCase
            const tag = match[1] ? match[1].toLowerCase() : match[2] ? match[2].toLowerCase() : '';
            if (tag) {
                counts[tag] = (counts[tag] || 0) + 1;
            }
        }
        return counts;
    }

    // Detect HTML
    function countHTMLTags(content) {
        const tagCounts = {};
        const openTags = [];
        const tagPattern = /<\/?(\w+)[^>]*>/g;
        let match;
        
        while ((match = tagPattern.exec(content)) !== null) {
            const tag = match[1].toLowerCase();

            if (match[0].startsWith('</')) {
                // Closing tag
                const lastOpenTag = openTags.pop();
                if (lastOpenTag === tag) {
                    tagCounts[tag] = (tagCounts[tag] || 0) + 1;
                }
            } else {
                // Opening tag
                openTags.push(tag);
            }
        }

        // Include any unmatched opening tags (they are not counted as pairs)
        return tagCounts;
    }

    // Detect HTML
    const htmlTags = countHTMLTags(content);
    if (Object.keys(htmlTags).length > 0) {
        return {
            contentType: 'HTML',
            tags: htmlTags
        };
    }

    // Detect CSS
    const cssTargetPattern = /(\w+)\s*\{/g;
    const cssTargets = countOccurrences(cssTargetPattern, content);
    if (Object.keys(cssTargets).length > 0) {
        return {
            contentType: 'CSS',
            cssTargets: cssTargets
        };
    }

    // Default to TEXT
    const lineCount = content.split('\n').length;
    return {
        contentType: 'TEXT',
        lineNumber: lineCount
    };
}

// Testing the function
console.log(analyzeContent("this is a test\nSeems to work"));
// Output: { contentType: 'TEXT', lineNumber: 2 }

console.log(analyzeContent("body{blabla} a{color:#fff} a{ padding:0}"));
// Output: { contentType: 'CSS', cssTargets: { body: 1, a: 2 } }

console.log(analyzeContent("<html><div></div><div></div></html>"));
// Output: { contentType: 'HTML', tags: { html: 1, div: 2 } }
