import React, { useState } from 'react';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Bot, User, Copy, Check, Sparkles } from 'lucide-react';

function CodeBlock({ node, inline, className, children, ...props }) {
  const [copied, setCopied] = useState(false);
  const match = /language-(\w+)/.exec(className || '');
  const lang = match ? match[1] : '';
  const codeContent = String(children).replace(/\n$/, '');

  const copyToClipboard = () => {
    navigator.clipboard.writeText(codeContent);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  if (!inline && match) {
    return (
      <div className="code-block-wrapper">
        <div className="code-header">
          <span>{lang}</span>
          <button className="copy-code-btn" onClick={copyToClipboard}>
            {copied ? <Check size={12} style={{ color: 'var(--success)' }} /> : <Copy size={12} />}
            <span>{copied ? 'Copied!' : 'Copy'}</span>
          </button>
        </div>
        <pre {...props}>
          <code>{children}</code>
        </pre>
      </div>
    );
  }

  return (
    <code className={className} {...props}>
      {children}
    </code>
  );
}

export default function MessageItem({ message }) {
  const isBot = message.role === 'MODEL';
  const timeFormatted = message.createdAt
    ? new Date(message.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    : '';

  return (
    <div className={`message-wrapper ${isBot ? 'model' : 'user'}`}>
      <div className={`message-avatar ${isBot ? 'bot' : 'user'}`}>
        {isBot ? <Sparkles size={18} /> : <User size={18} />}
      </div>

      <div className="message-bubble-wrapper">
        <div className="message-bubble">
          {isBot ? (
            <div className="markdown-content">
              <ReactMarkdown
                remarkPlugins={[remarkGfm]}
                components={{
                  code: CodeBlock,
                }}
              >
                {message.content}
              </ReactMarkdown>
            </div>
          ) : (
            <div style={{ whiteSpace: 'pre-wrap' }}>{message.content}</div>
          )}
        </div>

        <div className="message-meta">
          <span>{timeFormatted}</span>
          {isBot && message.candidateTokens ? (
            <>
              <span>•</span>
              <span>{message.candidateTokens} tokens</span>
            </>
          ) : null}
        </div>
      </div>
    </div>
  );
}
