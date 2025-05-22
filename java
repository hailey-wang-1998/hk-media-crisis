document.addEventListener("DOMContentLoaded", () => {
  // 🔹 Impact Number + 動畫
  const numberBoxes = document.querySelectorAll('.number-box');

  const numberObserver = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const box = entry.target;
        box.classList.add('visible');

        const numberEl = box.querySelector('.big-number');
        const text = numberEl.textContent.trim();
        const isPercent = text.includes('%');
        const target = parseFloat(text.replace(/[^\d.]/g, ''));

        let frame = 0;
        const duration = 1500;
        const frameRate = 30;
        const totalFrames = Math.round(duration / (1000 / frameRate));

        const update = () => {
          frame++;
          const progress = frame / totalFrames;
          const value = Math.round(progress * target);
          numberEl.textContent = isPercent ? `${value}%` : `${value}${text.includes('+') ? '+' : ''}`;
          if (frame < totalFrames) {
            requestAnimationFrame(update);
          } else {
            numberEl.textContent = text;
          }
        };
        requestAnimationFrame(update);

        numberObserver.unobserve(entry.target);
      }
    });
  }, { threshold: 0.3 });

  numberBoxes.forEach(box => numberObserver.observe(box));

  // 🔹 scroll-trigger 小人顯示// 🔹 scroll-trigger 小人顯示
  const yearEl = document.getElementById('year');
const peopleEl = document.getElementById('people');
const triggers = document.querySelectorAll('.scroll-trigger');

if (triggers.length > 0) {
  const maxString = triggers[0].dataset.people; // 固定用 2019 作為最大人數圖形
  const totalPeople = maxString.length;
  let currentYear = ""; // 🔹 防止同一年重複渲染

  const triggerObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const year = entry.target.dataset.year;
        const peopleString = entry.target.dataset.people;

        // 如果是同一年就不重繪
        if (year === currentYear) return;
        currentYear = year;

        yearEl.textContent = year;

        // 顯示比例
        const isSmallScreen = window.innerWidth <= 768;
        const scale = isSmallScreen ? 10 : 1;

        // 計數動畫
        const tallyEl = document.getElementById('member-tally');
        const finalCount = peopleString.length;

        let count = 0;
        const step = Math.max(1, Math.ceil(finalCount / 60));
        const interval = setInterval(() => {
          count += step;
          if (count >= finalCount) {
            count = finalCount;
            clearInterval(interval);
          }
          const note = isSmallScreen ? ' (1 icon = 10 members)' : '';
          tallyEl.innerHTML = `<strong>${count}</strong> members${note}`;

        }, 20);

        // 渲染小人圖（以 2019 為 base）
        let rendered = "";
        const shownCount = Math.ceil(totalPeople / scale);
        for (let i = 0; i < shownCount; i++) {
          const realIndex = Math.floor(i * scale);
          const char = maxString[realIndex] || "•";  // 固定 base shape
          const isPresent = realIndex < peopleString.length;
          rendered += `<span class="${isPresent ? 'person' : 'ghost'}">${char}</span>`;
        }
        peopleEl.innerHTML = rendered;
      }
    });
  }, { threshold: 0.5 });

  triggers.forEach(trigger => triggerObserver.observe(trigger));
}

  


  // 🔹 timeline .event 滑入動畫
  const timelineEvents = document.querySelectorAll('.event');
  const timelineObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        timelineObserver.unobserve(entry.target);
      }
    });
  }, { threshold: 0.3 });

  timelineEvents.forEach(event => timelineObserver.observe(event));

  // 🔹 timeline-item 淡入動畫
  const timelineItems = document.querySelectorAll('.timeline-item');
  const itemObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
        itemObserver.unobserve(entry.target);
      }
    });
  }, { threshold: 0.2 });
  timelineItems.forEach(item => itemObserver.observe(item));

  // 🔹 timeline-item 點擊更新說明文字
  document.addEventListener('click', (e) => {
    const el = e.target.closest('.timeline-item');
    if (!el) return;
    document.querySelectorAll('.timeline-item').forEach(item => item.classList.remove('active'));
    el.classList.add('active');
    const description = el.getAttribute('data-desc');
    const descBox = document.getElementById('timelineDescription');
    descBox.textContent = description;
    descBox.classList.add('visible');
  });
});
